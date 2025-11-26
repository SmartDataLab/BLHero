using System;
using System.Collections.Generic;
using System.Text;

namespace Opencsv {

    public class CSVParser {
        private readonly char separator;

        private readonly char quotechar;

        private readonly char escape;

        private readonly bool strictQuotes;

        private string pending;
        private bool inField = false;

        private readonly bool ignoreLeadingWhiteSpace;

        /**
        * The default separator to use if none is supplied to the constructor.
        */
        public static readonly char DEFAULT_SEPARATOR = ',';

        public static readonly int INITIAL_READ_SIZE = 128;

        /**
        * The default quote character to use if none is supplied to the
        * constructor.
        */
        public static readonly char DEFAULT_QUOTE_CHARACTER = '"';


        /**
        * The default escape character to use if none is supplied to the
        * constructor.
        */
        public static readonly char DEFAULT_ESCAPE_CHARACTER = '\\';

        /**
        * The default strict quote behavior to use if none is supplied to the
        * constructor
        */
        public static readonly bool DEFAULT_STRICT_QUOTES = false;

        /**
        * The default leading whitespace behavior to use if none is supplied to the
        * constructor
        */
        public static readonly bool DEFAULT_IGNORE_LEADING_WHITESPACE = true;

        /**
        * This is the "null" character - if a value is set to this then it is ignored.
        * I.E. if the quote character is set to null then there is no quote character.
        */
        public static readonly char NULL_CHARACTER = '\0';

        /**
        * Constructs CSVParser using a comma for the separator.
        */
        public CSVParser() : this(DEFAULT_SEPARATOR, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER) {
        }

        /**
        * Constructs CSVParser with supplied separator.
        *
        * @param separator the delimiter to use for separating entries.
        */
        public CSVParser(char separator) : this(separator, DEFAULT_QUOTE_CHARACTER, DEFAULT_ESCAPE_CHARACTER) {
        }


        /**
        * Constructs CSVParser with supplied separator and quote char.
        *
        * @param separator the delimiter to use for separating entries
        * @param quotechar the character to use for quoted elements
        */
        public CSVParser(char separator, char quotechar) : this(separator, quotechar, DEFAULT_ESCAPE_CHARACTER) {
        }

        /**
        * Constructs CSVReader with supplied separator and quote char.
        *
        * @param separator the delimiter to use for separating entries
        * @param quotechar the character to use for quoted elements
        * @param escape    the character to use for escaping a separator or quote
        */
        public CSVParser(char separator, char quotechar, char escape) : this(separator, quotechar, escape, DEFAULT_STRICT_QUOTES) {
            ;
        }

        /**
        * Constructs CSVReader with supplied separator and quote char.
        * Allows setting the "strict quotes" flag
        *
        * @param separator    the delimiter to use for separating entries
        * @param quotechar    the character to use for quoted elements
        * @param escape       the character to use for escaping a separator or quote
        * @param strictQuotes if true, characters outside the quotes are ignored
        */
        public CSVParser(char separator, char quotechar, char escape, bool strictQuotes) : this(separator, quotechar, escape, strictQuotes, DEFAULT_IGNORE_LEADING_WHITESPACE) {
        }

        /**
        * Constructs CSVReader with supplied separator and quote char.
        * Allows setting the "strict quotes" and "ignore leading whitespace" flags
        *
        * @param separator               the delimiter to use for separating entries
        * @param quotechar               the character to use for quoted elements
        * @param escape                  the character to use for escaping a separator or quote
        * @param strictQuotes            if true, characters outside the quotes are ignored
        * @param ignoreLeadingWhiteSpace if true, white space in front of a quote in a field is ignored
        */
        public CSVParser(char separator, char quotechar, char escape, bool strictQuotes, bool ignoreLeadingWhiteSpace) {
            if (anyCharactersAreTheSame(separator, quotechar, escape)) {
                throw new Exception("The separator, quote, and escape characters must be different!");
            }
            if (separator == NULL_CHARACTER) {
                throw new Exception("The separator character must be defined!");
            }
            this.separator = separator;
            this.quotechar = quotechar;
            this.escape = escape;
            this.strictQuotes = strictQuotes;
            this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        }

        private bool anyCharactersAreTheSame(char separator, char quotechar, char escape) {
            return isSameCharacter(separator, quotechar) || isSameCharacter(separator, escape) || isSameCharacter(quotechar, escape);
        }

        private bool isSameCharacter(char c1, char c2) {
            return c1 != NULL_CHARACTER && c1 == c2;
        }

        /**
        * @return true if something was left over from last call(s)
        */
        public bool IsPending() {
            return pending != null;
        }

        public string[] ParseLineMulti(string nextLine) {
            return ParseLine(nextLine, true);
        }

        public string[] ParseLine(string nextLine) {
            return ParseLine(nextLine, false);
        }

        /**
        * Parses an incoming String and returns an array of elements.
        *
        * @param nextLine the string to parse
        * @param multi
        * @return the comma-tokenized list of elements, or null if nextLine is null
        * @throws IOException if bad things happen during the read
        */
        private string[] ParseLine(string nextLine, bool multi) {

            if (!multi && pending != null) {
                pending = null;
            }

            if (nextLine == null) {
                if (pending != null) {
                    string s = pending;
                    pending = null;
                    return new string[]{s};
                } else {
                    return null;
                }
            }

            List<string> tokensOnThisLine = new List<string>();
            StringBuilder sb = new StringBuilder(INITIAL_READ_SIZE);
            bool inQuotes = false;
            if (pending != null) {
                sb.Append(pending);
                pending = null;
                inQuotes = true;
            }
            for (int i = 0; i < nextLine.Length; i++) {

                char c = nextLine[i];
                if (c == this.escape) {
                    if (isNextCharacterEscapable(nextLine, inQuotes || inField, i)) {
                        sb.Append(nextLine[i + 1]);
                        i++;
                    }
                } else if (c == quotechar) {
                    if (isNextCharacterEscapedQuote(nextLine, inQuotes || inField, i)) {
                        sb.Append(nextLine[i + 1]);
                        i++;
                    } else {
                        //inQuotes = !inQuotes;

                        // the tricky case of an embedded quote in the middle: a,bc"d"ef,g
                        if (!strictQuotes) {
                            if (i > 2 //not on the beginning of the line
                                    && nextLine[i - 1] != this.separator //not at the beginning of an escape sequence
                                    && nextLine.Length > (i + 1) &&
                                    nextLine[i + 1] != this.separator //not at the	end of an escape sequence
                                    ) {

                                if (ignoreLeadingWhiteSpace && sb.Length > 0 && isAllWhiteSpace(sb)) {
                                    sb.Length = 0;  //discard white space leading up to quote
                                } else {
                                    sb.Append(c);
                                    //continue;
                                }

                            }
                        }

                        inQuotes = !inQuotes;
                    }
                    inField = !inField;
                } else if (c == separator && !inQuotes) {
                    tokensOnThisLine.Add(sb.ToString());
                    sb.Length = 0; // start work on next token
                    inField = false;
                } else {
                    if (!strictQuotes || inQuotes) {
                        sb.Append(c);
                        inField = true;
                    }
                }
            }
            // line is done - check status
            if (inQuotes) {
                if (multi) {
                    // continuing a quoted section, re-append newline
                    sb.Append("\n");
                    pending = sb.ToString();
                    sb = null; // this partial content is not to be added to field list yet
                } else {
                    throw new Exception("Un-terminated quoted field at end of CSV line");
                }
            }
            if (sb != null) {
                tokensOnThisLine.Add(sb.ToString());
            }
            return tokensOnThisLine.ToArray();

        }

        /**
        * precondition: the current character is a quote or an escape
        *
        * @param nextLine the current line
        * @param inQuotes true if the current context is quoted
        * @param i        current index in line
        * @return true if the following character is a quote
        */
        private bool isNextCharacterEscapedQuote(string nextLine, bool inQuotes, int i) {
            return inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
                    && nextLine.Length > (i + 1)  // there is indeed another character to check.
                    && nextLine[i + 1] == quotechar;
        }

        /**
        * precondition: the current character is an escape
        *
        * @param nextLine the current line
        * @param inQuotes true if the current context is quoted
        * @param i        current index in line
        * @return true if the following character is a quote
        */
        protected bool isNextCharacterEscapable(string nextLine, bool inQuotes, int i) {
            return inQuotes  // we are in quotes, therefore there can be escaped quotes in here.
                    && nextLine.Length > (i + 1)  // there is indeed another character to check.
                    && (nextLine[i + 1] == quotechar || nextLine[i + 1] == this.escape);
        }

        /**
        * precondition: sb.length() > 0
        *
        * @param sb A sequence of characters to examine
        * @return true if every character in the sequence is whitespace
        */
        protected bool isAllWhiteSpace(StringBuilder sb) {
            bool result = true;
            for (int i = 0; i < sb.Length; i++) {
                char c = sb[i];

                if (c != ' ') {
                    return false;
                }
            }
            return result;
        }
    }
}
