using Opencsv;
using System.Collections.Generic;

namespace YYDesign {

    public class DesignReader {

        public static FileMeta Form(string text) {
            List<string[]> datas = CSVReader.Read(text);

            FileMeta fileMeta = new FileMeta();
            fileMeta.Macro = datas[0];
            fileMeta.Head = datas[1];
            fileMeta.Type = datas[2];
            fileMeta.ReadType = datas[3];
            fileMeta.Comment = datas[4];
            fileMeta.Data = datas.GetRange(5, datas.Count - 5);
            return fileMeta;
        }
    }
}