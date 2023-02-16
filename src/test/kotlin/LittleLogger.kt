abstract class LittleLogger {
    companion object{
        public fun addToReport(text: String){
            reportBlob = reportBlob+text
            print(text)
        }

        public fun addLineToReport(text: String){
            addToReport(text+"\r\n")
        }

        var reportBlob: String = ""
    }
}