#!/usr/bin/env python
# -*- coding:utf-8 -*-
# 
# pip install openpyxl
# pip install zipfile
# pip3 install pywin32
 
import sys
import os,stat
import time
import random
import hashlib

import json

import zipfile


if __name__ == '__main__':

    fold_gamecfg = "gamecfg"

    full_gamecfg_path = os.path.dirname( os.path.realpath(sys.argv[0]) )

    configSrc = os.path.join(full_gamecfg_path, fold_gamecfg)
    gamecfgBin = os.path.join(full_gamecfg_path, "gamecfg.bin")

    # zip to bin
    if os.path.isdir( configSrc ):
        for parent,dirnames,filenames in os.walk(configSrc): 
            if parent.count("src") > 0 :
                gamecfgBin = os.path.join(parent, "gamecfg.bin")

                lastModfTime = None

                if os.path.exists( gamecfgBin ):
                    # print("delete " + gamecfgBin)
                    os.remove(gamecfgBin)


                f = zipfile.ZipFile( gamecfgBin , mode="w", compression=zipfile.ZIP_DEFLATED, allowZip64=True, compresslevel=None, strict_timestamps=False)
                
                for filename in filenames:

                    if filename.count("json") and filename.count(".meta") <= 0:
                        wName = os.path.join(parent, filename)
                        os.utime(wName, (0, 0))
                        os.chmod(wName, stat.S_IRWXU|stat.S_IRWXG|stat.S_IRWXO)
                        # -rw-rw-rw-
                        f.write(wName, os.path.basename(wName))
                        # print("write %s" % wName)
                        print("write %s" % os.path.basename(wName))
                
                f.close()