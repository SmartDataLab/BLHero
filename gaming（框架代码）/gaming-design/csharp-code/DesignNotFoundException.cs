using System;

namespace YYDesign {

    public class DesignNotFoundException : Exception {
        private string message;
        private object[] args;
        public DesignNotFoundException(string message, params object[] args) {
            this.message = message;
            this.args = args;
        }

        public override string ToString() {
            return $"{message} can not find design object using args {args}";
        }
    }
}