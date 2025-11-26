# -*- coding: utf-8 -*-

import os
import config
from lxml import etree

MAP_PATH = config.MAP_PATH

CONNECTOR = " -> "


def check_layer_offset(layer_data, error_message):
    offset_x = int(layer_data.get("offsetx", "0"))
    offset_y = int(layer_data.get("offsety", "0"))
    if offset_x != 0:
        print(error_message, "x坐标不为0")
    if offset_y != 0:
        print(error_message, "y坐标不为0")
        # exit()


def check_layer_element(layer, error_message):
    # print(dir(layer))
    data = layer.find("data")
    elements = data.text.split(",")
    ret = {}
    for x in elements:
        if "\n" in x:
            continue
        if "0" == x:
            continue
        ret[x] = 1
    element_length = len(ret.keys())
    if element_length >= 8:
        err_msg = error_message + CONNECTOR + "图层内元素数量超过7个: " + str(element_length) + str(ret.keys())
        print(err_msg)


def check_group(group, error_message):
    for layer in group.findall("layer"):
        file_name = layer.attrib.get("name")
        # print(file_name)
        err_msg = error_message + CONNECTOR + layer.attrib.get("name")
        check_layer_offset(layer.attrib, err_msg)
        # check_layer_element(layer, err_msg)
        # exit()


def check_tmx_file(tmx_path, error_message):
    tmx = etree.parse(
        tmx_path, parser=etree.XMLParser(encoding="UTF-8", remove_blank_text=True)
    )
    root: etree._Element = tmx.getroot()
    groups = root.findall("group")
    if len(groups) > 0:
        for group in groups:
            check_group(group, error_message + CONNECTOR + group.attrib.get("name"))
    else:
        check_group(root, error_message)


def check_tmx():
    for file_name in os.listdir(MAP_PATH):
        if "map" not in file_name:
            continue
        file_path = os.path.join(MAP_PATH, file_name)
        if os.path.isfile(file_path):
            # print(file_path)
            check_tmx_file(file_path, file_name)


if __name__ == "__main__":
    check_tmx()
