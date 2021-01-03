package tika;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractEncodingDetectorParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class HelloParser extends AbstractEncodingDetectorParser {

    private static final Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("chf"));
    public static final String HELLO_MIME_TYPE = "application/chf";

    public Set<MediaType> getSupportedTypes(ParseContext context) {
        return SUPPORTED_TYPES;
    }


    public void parse(InputStream stream, ContentHandler handler, Metadata metadata, ParseContext context) throws IOException, SAXException, TikaException {

        try (AutoDetectReader reader = new AutoDetectReader(new CloseShieldInputStream(stream), metadata, this.getEncodingDetector(context))) {
            String incomingMime = metadata.get("Content-Type");
            MediaType mediaType = MediaType.TEXT_PLAIN;
            if (incomingMime != null) {
                MediaType tmpMediaType = MediaType.parse(incomingMime);
                if (tmpMediaType != null) {
                    mediaType = tmpMediaType;
                }
            }

            Charset charset = reader.getCharset();
            MediaType type = new MediaType(mediaType, charset);
            metadata.set("Content-Type", type.toString());
            metadata.set("Content-Encoding", charset.name());
            XHTMLContentHandler xhtml = new XHTMLContentHandler(handler, metadata);
            xhtml.startDocument();
            char[] buffer = new char[4096];

            String s = reader.readLine();

            metadata.set("Header", s);

            s = reader.readLine();

            xhtml.newline();


            int count=0;

          while (s != null) {
              xhtml.startElement("p"+count);
                xhtml.characters(s.toCharArray(), 0, s.length());

              xhtml.endElement("p"+count++);
              xhtml.newline();
                s = reader.readLine();
            }

  /*          int n = reader.read(buffer);

           while(n!=-1)
            {
                xhtml.startElement("p"+count);
                xhtml.characters(buffer, 0, n);
                xhtml.endElement("p"+count++);
                n = reader.read(buffer);
            }

       */

            xhtml.endDocument();
        }


    }

}


