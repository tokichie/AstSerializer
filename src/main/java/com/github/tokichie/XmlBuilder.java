package com.github.tokichie;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tokitake on 2015/07/29.
 */
public class XmlBuilder {

  private StringBuilder sb;

  public XmlBuilder() {
    sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
  }

  public void addNode(ASTNode node, int depth, HashMap<String, String> attributes) {
//    List<String> ExpressionNodes = new ArrayList<String>(Arrays.asList(
//        "Name", "NumberLiteral", "NullLiteral", "Assignment", "InfixExpression", "PostfixExpression", "PrefixExpression"));
//    System.out.println(node.getClass().getSuperclass().getSimpleName());
//    if (ExpressionNodes.contains(node.getClass().getSuperclass().getSimpleName()) ||
//        ExpressionNodes.contains(node.getClass().getSimpleName())) {
//      name = "ExpressionToken";
//    }
    String name = node.getClass().getSimpleName();
    indent(depth);
    sb.append("<").append(name);
    if (attributes != null) {
      for (Map.Entry<String, String> attribute : attributes.entrySet()) {
        sb.append(" ").append(attribute.getKey()).append("=\"").append(attribute.getValue())
            .append("\"");
      }
    }
    sb.append(">\n");
  }

  public void addContent(String content, int depth) {
    sb.delete(sb.length() - 1, sb.length());
    sb.append(escape(content.trim()));
  }

  public void addProperty(String propertyId, String token, int depth, HashMap<String, String> attributes) {
    sb.delete(sb.length() - 1, sb.length());
    sb.append("<").append(propertyId);
    if (attributes != null) {
      for (Map.Entry<String, String> attribute : attributes.entrySet()) {
        sb.append(" ").append(attribute.getKey()).append("=\"").append(attribute.getValue())
            .append("\"");
      }
    }
    sb.append(">").append(escape(token.trim())).append("</").append(propertyId).append(">");
  }

  public String escape(String content) {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < content.length(); i++) {
      char c = content.charAt(i);
      if (c == '<')
        buffer.append("&lt;");
      else if (c == '>')
        buffer.append("&gt;");
      else if (c == '&')
        buffer.append("&amp;");
      else if (c == '"')
        buffer.append("&quot;");
      else if (c == '\'')
        buffer.append("&apos;");
      else
        buffer.append(c);
    }
    return buffer.toString();
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

  public void save() {
    try {
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
      writer.write(sb.toString());
      writer.newLine();
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
