package ui

class FolderButtonTest extends groovy.util.GroovyTestCase {
    void testHTMLConverter() {
        assertEquals("<html><center>012345</center></html>", FolderButton.toMultilineHTML("012345"));
        assertEquals("<html><center>012345678901</center></html>",
                FolderButton.toMultilineHTML("012345678901"));
        assertEquals("<html><center>012345678901<br>2</center></html>",
                FolderButton.toMultilineHTML("0123456789012"));
        assertEquals("<html><center>012345678901<br>234567890123</center></html>",
                FolderButton.toMultilineHTML("012345678901234567890123"));
        assertEquals("<html><center>012345678901<br>23456789012</center></html>",
                FolderButton.toMultilineHTML("01234567890123456789012"));
        assertEquals("<html><center>012345678901<br>234567890123<br>4</center></html>",
                FolderButton.toMultilineHTML("0123456789012345678901234"));
    }
}
