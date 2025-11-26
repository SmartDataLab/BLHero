def get_attrib(node, key):
    if node.attrib is None:
        return ""
    return node.attrib.get(key)


def set_attrib(node, key, value):
    if node.attrib is None:
        return
    node.attrib[key] = value
