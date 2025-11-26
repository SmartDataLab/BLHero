import configparser

config = configparser.ConfigParser()
config.read("config.ini")

# 获取 database 节中的所有变量
database = config["map-editor-settings"]

SYNC_TO_WORKBENCH = database["SYNC_TO_WORKBENCH"]
MAP_PATH = database["MAP_PATH"]
TARGET_FOLDER = database["TARGET_FOLDER"]

TEMP_FOLDER = ".\\temp"
CLIENT_DATA_FOLDER = ".\\client\\"
CLIENT_JSON_FOLDER = ".\\client\\json\\"
CLIENT_MAP_FOLDER = ".\\client\\map\\"
SERVER_DATA_FOLDER = ".\\server\\"
MD5_FILE_PATH = ".\\md5_tmx.json"
