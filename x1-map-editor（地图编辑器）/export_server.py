# -*- coding: utf-8 -*-
import math
import os
import shutil
import re
import numpy as np

from lxml import etree

from config import *
from stm import *


FILTER_KEYS_LIST = ["monster_point", "resource_point", "map_monster_point"]

FOG_AREA = "fog_area"

fog_layer_data = dict()
fog_id_area_list = []

class FogAreaObj:
    def __init__(self, id, area_list):
        self.id = int(id)
        self.area_list = area_list

def check_json_folder():
    """检查本地临时文件夹"""
    if os.path.exists(SERVER_DATA_FOLDER):
        shutil.rmtree(SERVER_DATA_FOLDER)
    os.mkdir(SERVER_DATA_FOLDER)


def export_tmx_to_json(file_path, target_name):
    print(">>export_tmx_to_json:",file_path)
    json_data = dict()

    tmx = etree.parse(
        file_path, parser=etree.XMLParser(encoding="UTF-8", remove_blank_text=True)
    )
    root: etree._Element = tmx.getroot()

    for objectgroup in root.findall("objectgroup"):
        for object in objectgroup:
            data = object.attrib

            name = data.get("name")
            id = data.get("id")
            if name in FILTER_KEYS_LIST:
                for properties in object.findall("properties"):
                    x = math.floor(float(data.get("x")))
                    y = math.floor(float(data.get("y")))
                    grid_x = math.floor(x / 120)
                    grid_y = math.floor(y / 120)
                    fog_area = 0

                    for fogAreaObj in fog_id_area_list:
                        fog_id = fogAreaObj.id
                        # print(">>>>fog_id:",fog_id)
                        impassArea = fogAreaObj.area_list
                        if [grid_x, grid_y] in impassArea:
                            fog_area = fog_id
                            break

                    for property in properties.findall("property"):
                        property_name = property.attrib.get("name")
                        property_value = property.attrib.get("value")
                        data[property_name] = property_value
                        data["fog_area"] = str(fog_area)
                    #底下得注释先别删，留着以后验证
                    if data["fog_area"] == "0":
                        print(">>>data:",data,[grid_x, grid_y])

                if json_data.get(name) is None:
                    json_data[name] = []
                json_data[name].append(data)

    if len(json_data.keys()) > 0:
        target_path = os.path.join(SERVER_DATA_FOLDER, target_name)
        with open(target_path, "w+") as fp:
            fp.write(str(json_data).replace("'", '"'))


def main_scene_map(mapId):
    for name in os.listdir(TEMP_FOLDER):
        file_name, postfix = os.path.splitext(name)
        if postfix != ".tmx" or not mapId in file_name:
            continue
        file_path = os.path.join(TEMP_FOLDER, name)
        export_tmx_to_json(file_path, file_name + ".json")

def dungeon_map():
    for file_name in os.listdir(MAP_PATH):
        dir_path = os.path.join(MAP_PATH, file_name)

        if os.path.isdir(dir_path) and "tmx" in file_name:
            scene_id = file_name.split("_")[1]
            if int(scene_id) > 2010000:
                file_path = os.path.join(dir_path, scene_id) + ".tmx"
                export_tmx_to_json(file_path, scene_id + ".json")

def deal_map(mainId):
    print("[开始计算迷雾区域] ==================================")
    for file_name in os.listdir(MAP_PATH):
        dir_path = os.path.join(MAP_PATH, file_name)
        if os.path.isdir(dir_path) and "tmx_" in file_name:
            for sub_file_name in os.listdir(dir_path):
                sub_file_path = os.path.join(dir_path, sub_file_name)
                if os.path.splitext(sub_file_path)[1] == ".tmx":
                    if sub_file_name == mainId+".tmx":
                        fog_layer_data.clear()
                        fog_id_area_list = []
                        merge_map_render(sub_file_path, sub_file_name)
                        export_area_json(sub_file_path, sub_file_name)
    print("[结束计算迷雾区域]] ==================================")

def export_area_json(file_path, file_name):
    tmx = etree.parse(
        file_path, parser=etree.XMLParser(encoding="UTF-8", remove_blank_text=True)
    )
    area_layers = dict()
    root: etree._Element = tmx.getroot()
    all_group = root.findall("group")

    # 获取一个没有group的tmx模板
    for group in all_group:
        root.remove(group)

    # 计算云层轮廓
    for fog_layer_name in fog_layer_data.keys():
        fog_id = fog_layer_name.split("#")[1]
        matrix = fog_layer_data.get(fog_layer_name)
        matrix = np.array(matrix)
        fog_impassable_grid_list = []

        x_list = [] #记录迷雾最左边的一列x
        y_list = [] #记录迷雾最下边的y
        for i in range(len(matrix)):
            for j in range(len(matrix[i])):
                if matrix[i][j] != "0":
                    if j - 1 not in x_list:
                        x_list.append(j - 1)
                    if i - 1 not in y_list:
                        y_list.append(i - 1)
                    fog_impassable_grid_list.append([j-1,i-1])
        #记录获取该片迷雾最左边那一排的坐标
        max_x_dict = dict()
        for arr in fog_impassable_grid_list:
            if arr[0] in x_list:
                if max_x_dict.get(arr[0]) == None :
                    max_x_dict[arr[0]] = arr[1]
                else:
                    if max_x_dict[arr[0]] < arr[1]:
                        max_x_dict[arr[0]] = arr[1]
        #记录获取该片迷雾最下面那一排的坐标 [x和y翻转]
        max_y_dict = dict()
        for arr in fog_impassable_grid_list:
            if arr[1] in y_list:
                if max_y_dict.get(arr[1]) == None:
                    max_y_dict[arr[1]] = arr[0]
                else:
                    if max_y_dict[arr[1]] < arr[0]:
                        max_y_dict[arr[1]] = arr[0]
        #补这两个是，因为迷雾加了阴影
        for x in max_x_dict:
            fog_impassable_grid_list.append([x,max_x_dict.get(x) + 1])
        for y in max_y_dict:
            fog_impassable_grid_list.append([max_y_dict.get(y) + 1, y])

        # print(">>fog_id_len:",fog_id,fog_impassable_grid_list, len(fog_impassable_grid_list))
        fogAreaObj = FogAreaObj(fog_id,fog_impassable_grid_list)
        fog_id_area_list.append(fogAreaObj)
    fog_id_area_list.sort(key=lambda obj: obj.id,reverse=True)

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


if __name__ == "__main__":
    check_json_folder()
    main_scene_id_list = ["2000001","2000002","2000003", "2000010"]
    for scene_id in main_scene_id_list:   
        fog_id_area_list = []
        deal_map(scene_id)
        main_scene_map(scene_id)
    fog_id_area_list = []
    dungeon_map()


