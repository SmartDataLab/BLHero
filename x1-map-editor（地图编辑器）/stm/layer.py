import csv

from stm.attrib import get_attrib, set_attrib


def get_layer_id(layer):
    return get_attrib(layer, "id")


def get_layer_name(layer):
    return get_attrib(layer, "name")

def get_layer_region(layer):
    propertie = layer.find("properties")
    if propertie is not None:
        region_id = get_attrib(propertie[0], "value")
        return region_id
    return 1


def set_layer_name(layer, name):
    set_attrib(layer, "name", name)


def get_layer_size(layer):
    width = get_attrib(layer, "width")
    height = get_attrib(layer, "height")
    return {
        "width": width,
        "height": height,
    }


def get_layer_csv(layer):
    ret = []
    data = layer.find("data")
    if data is not None and data.text:
        csv_reader = csv.reader(data.text.splitlines())
        next(csv_reader)  # 跳过标题行
        for row in csv_reader:
            tmp = []
            for column in row:
                if len(column) > 0:
                    tmp.append(column)
            ret.append(tmp)
    else:
        # print("没有CSV", get_layer_name(layer))
        pass
    return ret


def merge_layer(a, b):
    if a is None:
        return b
    size = get_layer_size(a)
    sizeb = get_layer_size(b)
    aData = get_layer_csv(a)
    bData = get_layer_csv(b)
    aName = get_layer_name(a)
    bName = get_layer_name(b)
    bId = get_layer_id(b)

    # debug = dict()

    for y in range(int(size.get("height"))):
        for x in range(int(size.get("width"))):
            # print(bName, bId, x, y)
            # debug[bData[y][x]] = 1
            # debug[aData[y][x]] = 1

            try:
                if bData[y][x] == aData[y][x]:
                    continue

                if bData[y][x] == "0" and aData[y][x] != "0":
                    continue
                if aData[y][x] == "0" and bData[y][x] != "0":
                    aData[y][x] = bData[y][x]
                    continue
            except:
                print("图层报错:", aName,size.get("height"),size.get("width"),bName,sizeb.get("height"),sizeb.get("width"))
                return a

            # if bData[y][x] == aData[y][x]:
            #     continue

            # if bData[y][x] == "0" and aData[y][x] != "0":
            #     continue
            # if aData[y][x] == "0" and bData[y][x] != "0":
            #     aData[y][x] = bData[y][x]
            #     continue
            # print("[%s] 瓦片重合: %d %d" % (bName, x, y), aData[y][x], bData[y][x])
    # print(bName, debug.keys())

    ret = "\n"
    for row in aData:
        ret = ret + (",".join(str(k) for k in row)) + ",\n"
    ret = ret[0:-2] + "\n"

    data = a.find("data")
    data.text = ret
    return a


def merge_object_layer(a, b):
    if a is None:
        return b
    for x in b.getchildren():
        a.append(x)
    return a
