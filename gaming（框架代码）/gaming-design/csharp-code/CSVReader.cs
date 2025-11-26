using System.IO;
using Opencsv;
using System.Collections.Generic;

namespace Opencsv {

    public class CSVReader {
        public static List<string[]> Read(string text) {
            CSVParser csvParser = new CSVParser();

            List<string[]> result = new List<string[]>();

            StringReader sr = new StringReader(text);
            string nextLine;
            while((nextLine = sr.ReadLine()) != null) {
                string[] data = csvParser.ParseLineMulti(nextLine);
                result.Add(data);
            }
            sr.Close();
            return result;
        }
    }
}
