# -*- coding: utf-8 -*-

import hashlib
import json
import os
import re
import shutil

import numpy as np
from lxml import etree

from config import *
from stm import *

DIR8 = [
    (-1, -1),
    (-1, 0),
    (-1, 1),
    (0, -1),
    (0, 1),
    (1, -1),
    (1, 0),
    (1, 1),
]

fog_layer_data = dict()
region_rect_data = dict()


def _round(x):
    return round(x, 2)

def check_cache_folder():
    """检查本地临时文件夹"""
    if os.path.exists(TEMP_FOLDER):
        shutil.rmtree(TEMP_FOLDER)
    os.mkdir(TEMP_FOLDER)

    if os.path.exists(CLIENT_DATA_FOLDER):
        shutil.rmtree(CLIENT_DATA_FOLDER)
    os.mkdir(CLIENT_DATA_FOLDER)
    os.mkdir(CLIENT_MAP_FOLDER)
    os.mkdir(CLIENT_JSON_FOLDER)

    # 复制 img_xxxx
    for file_name in os.listdir(MAP_PATH):
        file_path = os.path.join(MAP_PATH, file_name)
        if os.path.isdir(file_path) and "img" in file_name:
            # print(file_path, CLIENT_MAP_FOLDER)
            target_name = os.path.join(CLIENT_MAP_FOLDER, file_name)
            shutil.copytree(file_path, target_name)


RENDER_LAYER = [
    "ground",
    "ground2",
    "road",
    "sand",
    "water",
    "mountain",
]
OBJECT_LAYER = [
    "object_low",
    "entity",
    "object_high",
    "object",
    "normal",
    "wasteland",
]

IMPASSABLE_AREA = "impassable_area"
AREA_LAYER = [
    IMPASSABLE_AREA,
    "safe_area",
    "outside_area",
    # 'cover_area'
]
FOG_AREA = "fog_area"

print("ZZZZZZZZZZZZZZZZZZZZZZZZ")
def export_area_json(file_path, file_name):
    """导出各区域 Json 文件"""
    tmx = etree.parse(
        file_path, parser=etree.XMLParser(encoding="UTF-8", remove_blank_text=True)
    )

    area_layers = dict()
    root: etree._Element = tmx.getroot()
    all_group = root.findall("group")

    # 获取一个没有group的tmx模板
    for group in all_group:
        root.remove(group)

    def deal_object_layer(layer, target_name, func):
        for layer in layer.getchildren():
            if target_name == get_layer_name(layer):
                if target_name == IMPASSABLE_AREA:
                    region_id = get_layer_region(layer)
                    region_rect_data[region_id] = get_layer_csv(layer)
                area_layers[target_name] = func(area_layers.get(target_name), layer)

    def calc_data_layer(node):
        for target_name in AREA_LAYER:
            deal_object_layer(node, target_name, merge_layer)

    # 计算数据层
    if len(all_group) > 0:
        for group in all_group:
            calc_data_layer(group)
    else:
        calc_data_layer(root)

    area_layer_keys = sorted(area_layers.keys())
    ret = dict()
    for key in area_layer_keys:
        layer = area_layers.get(key)

        ret[key] = []
        for row in get_layer_csv(layer):
            tmp = []
            for x in row:
                tmp.append(int(x))

            ret[key].append(tmp)

    # 计算不可行走区域轮廓
    impassable_area_data = ret.get(IMPASSABLE_AREA)
    # print(" ============= ", ret.keys())
    if impassable_area_data:
        edge_tile_list = []
        matrix = np.array(impassable_area_data)
        matrix = np.clip(matrix, 0, 1)
        rows, cols = matrix.shape
        for y in range(rows):
            for x in range(cols):
                if matrix[y][x] == 1:
                    for dx, dy in DIR8:
                        nx, ny = x + dx, y + dy
                        # 检查坐标是否在矩阵范围内且值为0
                        if 0 <= ny < rows and 0 <= nx < cols and matrix[ny][nx] == 0:
                            edge_tile_list.append([x, y])
                            break
        # print(">>>edge_tile_list:",edge_tile_list)
        ret["edge_impassable_area"] = edge_tile_list

    # 计算云层轮廓
    for fog_layer_name in fog_layer_data.keys():
        fog_id = fog_layer_name.split("#")[1]
        #云层边界 用来画地图边界格子
        edge_fog_list = []
        #也是云层边界 用来做云层显示的4叉树数据
        rect_list = []
        #云层数据，x,y,img
        fog_pos_img_list = []

        matrix = fog_layer_data.get(fog_layer_name)
        matrix = np.array(matrix)

        rows, cols = matrix.shape
        minX = maxX = minY = maxY = 0
        # 云层数据 把csv转成2维数组
        fog_grid_list = [[0 for _ in range(cols)] for _ in range(rows)]

        for y in range(rows):
            for x in range(cols):
                if matrix[y][x] != "0":
                    fog_pos_img_list.append([x, y, matrix[y][x]])
                    for dx, dy in DIR8:
                        nx, ny = x + dx, y + dy
                        # 检查坐标是否在矩阵范围内且值为0
                        if 0 <= ny < rows and 0 <= nx < cols and matrix[ny][nx] == "0":
                            edge_fog_list.append([x, y])
                            minX = x if minX == 0 else min(minX,x)
                            maxX = max(maxX,nx)
                            minY = ny if minY == 0 else min(minY,ny)
                            maxY = max(maxY,ny)
                            break
        rect_list.extend([minX, maxX, minY, maxY, (minX + maxX) / 2, (minY + maxY) / 2])
        ret["edge_fog_area"] = ret.get("edge_fog_area", {})
        ret["edge_fog_area"][fog_id] = edge_fog_list
        ret["fog_rect"] = ret.get("fog_rect",{})
        ret["fog_rect"][fog_id] = rect_list
        ret["fog_pos_img"] = ret.get("fog_pos_img", {})
        ret["fog_pos_img"][fog_id] = fog_pos_img_list

        for i in range(len(matrix)):
            for j in range(len(matrix[i])):
                if matrix[i][j] != "0":
                    fog_grid_list[i][j] = 1
                else:
                    fog_grid_list[i][j] = 0
        ret["fog_grid_list"] = ret.get("fog_grid_list", {})
        ret["fog_grid_list"][fog_id] = fog_grid_list

    #计算区域方块
    for region_id in region_rect_data.keys():
        rect_list = []
        matrix = region_rect_data.get(region_id)
        matrix = np.array(matrix)

        rows, cols = matrix.shape
        minX = maxX = minY = maxY = 0
        for y in range(rows):
            for x in range(cols):
                if matrix[y][x] != "0":
                    for dx, dy in DIR8:
                        nx, ny = x + dx, y + dy
                        # 检查坐标是否在矩阵范围内且值为0
                        if 0 <= ny < rows and 0 <= nx < cols and matrix[ny][nx] == "0":
                            minX = x if minX == 0 else min(minX,x)
                            maxX = max(maxX,nx)
                            minY = ny if minY == 0 else min(minY,ny)
                            maxY = max(maxY,ny)
                            break
        rect_list.extend([minX, maxX, minY, maxY])
        ret["region_area_rect"] = ret.get("region_area_rect",{})
        ret["region_area_rect"][region_id] = rect_list

    # 计算多边形
    polygon_tile_map = dict()
    polygon_layer_map = dict()
    polygon_object_map = dict()

    def deal_polygon(layer):
        for layer in layer.getchildren():
            csv = get_layer_csv(layer)
            keys = polygon_tile_map.keys()
            if csv:
                # Tile Layer
                y = 0
                for row in csv:
                    x = 0
                    for gid in row:
                        _gid = int(gid)
                        if _gid in keys:
                            polygon_layer_map[_gid] = polygon_layer_map.get(_gid, [])
                            polygon_layer_map[_gid].append([x + 1, y + 1])
                        x += 1
                    y += 1
                pass
            else:
                # Object Layer
                for object in layer.getchildren():
                    gid = get_attrib(object, "gid")
                    if gid:
                        _gid = int(gid)
                        if _gid in keys:
                            x = _round(float(get_attrib(object, "x")))
                            y = _round(float(get_attrib(object, "y")))
                            polygon_object_map[_gid] = polygon_object_map.get(_gid, [])
                            polygon_object_map[_gid].append([x, y])

    for tile_set in root.findall("tileset"):
        first_gid = int(get_attrib(tile_set, "firstgid"))
        ret["first_gid_list"] = ret.get("first_gid_list", {})
        ret["first_gid_list"][first_gid] = first_gid
        for tile in tile_set.findall("tile"):
            id = int(get_attrib(tile, "id")) + first_gid

            object_group = tile.find("objectgroup")
            if object_group is not None:
                image = tile.find("image")
                if image is not None:
                    width = int(get_attrib(image, "width"))
                    height = int(get_attrib(image, "height"))
                else:
                    width = 200
                    height = 120

                object = object_group.find("object")
                ox = float(get_attrib(object, "x"))
                oy = float(get_attrib(object, "y"))

                # 多边形
                polygon = object.find("polygon")
                if polygon is not None:
                    points = get_attrib(polygon, "points")
                    point_str_list = points.split(" ")
                    points_int_list = []
                    for point_str in point_str_list:
                        v2 = point_str.split(",")
                        x = float(v2[0])
                        y = float(v2[1])
                        x = _round(ox + x - width / 2)
                        y = _round(height - (oy + y) - height / 2)
                        points_int_list.append([x, y])
                    polygon_tile_map[id] = {
                        "width": width,
                        "height": height,
                        "points": points_int_list,
                    }
                # TODO: 矩形
                pass
                # TODO: 圆形
                pass

    if len(all_group) > 0:
        for group in all_group:
            deal_polygon(group)
    else:
        deal_polygon(root)

    if len(polygon_tile_map.keys()) > 0:
        ret["polygon_tile"] = polygon_tile_map
    if len(polygon_layer_map.keys()) > 0:
        ret["polygon_layer"] = polygon_layer_map
    if len(polygon_object_map.keys()) > 0:
        ret["polygon_object"] = polygon_object_map

    # 计算序列帧
    pass

    # make client map file json
    if len(ret.keys()) > 0:
        json_name = os.path.splitext(file_name)[0] + ".json"
        json_path = os.path.join(CLIENT_JSON_FOLDER, json_name)
        with open(json_path, "w") as fp:
            fp.write(json.dumps(ret, ensure_ascii=False, separators=(",", ":")))
        # print("[Json]", json_path)


def merge_map_render(file_path, file_name):
    tmx = etree.parse(
        file_path, parser=etree.XMLParser(encoding="UTF-8", remove_blank_text=True)
    )

    layers = dict()
    root: etree._Element = tmx.getroot()
    all_group = root.findall("group")

    # 获取一个没有group的tmx模板
    for group in all_group:
        root.remove(group)

    def deal_layer(node, target_name, func):
        for layer in node.getchildren():
            id = get_layer_id(layer)
            if id is None:
                continue
            current_layer_name = get_layer_name(layer)
            simple_name = current_layer_name

            pattern = re.compile(r"[\d\_]+(.*)")  # example: 1_1_1water | 1_1_1ground2
            m = pattern.match(current_layer_name)
            if m is not None:
                simple_name = m.group(1)
            else:
                pass
            if target_name == simple_name:
                set_layer_name(layer, simple_name)
                set_attrib(layer, "visible", "1")
                set_attrib(layer, "locked", "1")
                layers[simple_name] = func(layers.get(simple_name), layer)
                continue

            if "fog" in target_name and target_name in simple_name:
                set_layer_name(layer, simple_name)
                set_attrib(layer, "visible", "0")
                set_attrib(layer, "locked", "1")
                if layers.get(simple_name) is not None:
                    print(
                        "[!!!!!!!!!!!!!!!!!!!] 迷雾图层重复:",
                        file_name,
                        target_name,
                        simple_name,
                    )
                layers[simple_name] = layer
                fog_layer_data[simple_name] = get_layer_csv(layer)

    def deal_node(node):
        for target_name in RENDER_LAYER:
            deal_layer(node, target_name, merge_layer)
        for target_name in OBJECT_LAYER:
            deal_layer(node, target_name, merge_object_layer)
        deal_layer(node, FOG_AREA, None)

    if len(all_group) > 0:
        for group in all_group:
            deal_node(group)
    else:
        deal_node(root)

    fog_keys = sorted(layers.keys(), reverse=True)
    for key in fog_keys:
        if FOG_AREA in key:
            root.append(layers.get(key))
    for layer_name in RENDER_LAYER:
        if layers.get(layer_name) is not None:
            root.append(layers.get(layer_name))
    for layer_name in OBJECT_LAYER:
        if layers.get(layer_name) is not None:
            root.append(layers.get(layer_name))

    # check dir
    dir_name = "tmx_" + os.path.splitext(file_name)[0]
    target_dir = os.path.join(CLIENT_MAP_FOLDER, dir_name)
    os.makedirs(target_dir, exist_ok=True)
    target_path = os.path.join(target_dir, file_name)

    # 保存合并后的TMX
    tmx.write(target_path, encoding="utf-8")


def split_map(file_path):
    """分离地图"""
    tmx = etree.parse(
        file_path, parser=etree.XMLParser(encoding="UTF-8", remove_blank_text=True)
    )
    root: etree._Element = tmx.getroot()

    all_group = root.findall("group")
    # 获取一个没有group的tmx模板
    for x in all_group:
        root.remove(x)

    # 拆分group到单独的tmx文件
    for x in all_group:
        file_name = x.attrib.get("name")
        path_name = os.path.join(TEMP_FOLDER, file_name + ".tmx")

        layers = x.getchildren()

        # 添加每个group里的图层
        for layer in layers:
            root.append(layer)

        # 保存
        tmx.write(path_name, encoding="utf-8")
        # print("[save] path_name", path_name)

        # 删除差异部分
        for layer in layers:
            root.remove(layer)


def md5_checksum(file_path):
    """计算文件的MD5哈希值"""
    with open(file_path, "rb") as f:
        return hashlib.md5(f.read() + bytes(file_path, encoding="utf-8")).hexdigest()


def recursive_copy(src_dir, dst_dir, json_file, file_checksums):
    """递归复制一个文件夹内的文件，只有md5不同的才复制，并将结果保存到一个json文件中"""
    # 确保目标文件夹存在
    os.makedirs(dst_dir, exist_ok=True)

    # 遍历源文件夹中的所有文件和目录
    for item in os.listdir(src_dir):
        src_path = os.path.join(src_dir, item)
        dst_path = os.path.join(dst_dir, item)

        # 如果是文件，计算其哈希值并检查是否已经存在
        if os.path.isfile(src_path):
            checksum = md5_checksum(src_path)
            # print(item, checksum)
            # if item not in file_checksums or file_checksums[item] != checksum:
            shutil.copy2(src_path, dst_path)  # 复制文件并保留元数据
                # file_checksums[item] = checksum
            # print(f"Copied {item}, md5: {checksum}")
            # else:
                # print(f"Skipped {item}, md5: {checksum}")
                # pass
        # 如果是目录，递归处理
        elif os.path.isdir(src_path):
            recursive_copy(src_path, dst_path, json_file, file_checksums)
        # 忽略其他类型的文件和目录
        else:
            print(f"Ignored {item}")


def sync_to_workbench():
    if SYNC_TO_WORKBENCH != "true":
        return

    file_checksums = {}
    if os.path.exists(MD5_FILE_PATH):
        with open(MD5_FILE_PATH, "r") as f:
            file_checksums = json.load(f)

    recursive_copy(CLIENT_MAP_FOLDER, TARGET_FOLDER, MD5_FILE_PATH, file_checksums)
    # 将文件哈希值保存到json文件中
    with open(MD5_FILE_PATH, "w") as f:
        json.dump(file_checksums, f, sort_keys=True, indent=4)


def deal_map():
    print("[start parse]把迷雾数据写到配置表==================================")
    for file_name in os.listdir(MAP_PATH):
        dir_path = os.path.join(MAP_PATH, file_name)
        if os.path.isdir(dir_path) and "tmx_" in file_name:
            for sub_file_name in os.listdir(dir_path):
                sub_file_path = os.path.join(dir_path, sub_file_name)
                if os.path.splitext(sub_file_path)[1] == ".tmx":
                    fog_layer_data.clear()
                    region_rect_data.clear()
                    split_map(sub_file_path)
                    merge_map_render(sub_file_path, sub_file_name)
                    export_area_json(sub_file_path, sub_file_name)

    print("[finish parse]==================================")

if __name__ == "__main__":
    check_cache_folder()
    deal_map()
    sync_to_workbench()
