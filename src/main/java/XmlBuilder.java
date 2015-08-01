import org.apache.commons.lang.StringUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by tokitake on 2015/07/29.
 */
public class XmlBuilder {

  private StringBuilder sb;

  public XmlBuilder() {
    sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
  }

  public void addNode(String name, String content, int depth, HashMap<String, String> attributes) {
    indent(depth);
    sb.append("<").append(name);
    if (attributes != null) {
      for (Map.Entry<String, String> attribute : attributes.entrySet()) {
        sb.append(" ").append(attribute.getKey()).append("=\"").append(attribute.getValue())
            .append("\"");
      }
    }
    sb.append(">");
    if (content != null) {
      indent(depth + 1);
      sb.append(content.trim());
    } else {
      sb.append("\n");
    }
  }

  public void closeNode(String name, int depth) {
    indent(depth);
    sb.append("</").append(name).append(">\n");
  }

  private void indent(int depth) {
    for (int i = 0; i < depth; i++) {
      sb.append("  ");
    }
  }

  public void save(String path) {
    try {
      File file = new File(path);
      BufferedWriter writer = new BufferedWriter(new FileWriter(file));
      writer.write(sb.toString());
      writer.newLine();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}