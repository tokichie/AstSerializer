package com.github.tokichie;

/**
 * Created by tokitake on 2015/07/23.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

public class AstTest {
  public static final String StartLineName = "startline";
  public static final String StartPositionName = "startpos";
  public static final String EndLineName = "endline"; // Inclusive
  public static final String EndPositionName = "endpos"; // Inclusive

  public static void main(String[] args) {
    try {
      StringBuffer sb = new StringBuffer();
//      BufferedReader br = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream("Test3.java")));
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line + "\n");
      }

      // Create AST Parser
      ASTParser parser = ASTParser.newParser(AST.JLS3);
      parser.setSource(sb.toString().toCharArray());
      CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());

      XmlBuilder xb = new XmlBuilder();
      StringBuilder out = new StringBuilder();
      scan(unit, xb, unit, out, 0);
      xb.save();
      System.out.println(out);

      System.out.println("Done !");
    } catch (FileNotFoundException e) {
      return;
    } catch (IOException e) {
      return;
    }
  }

  private static String indent(int depth) {
    String s = "";
    for (int i = 0; i < depth * 2; i++) {
      s += " ";
    }
    return s;
  }

  private static void scan(final CompilationUnit unit, final XmlBuilder xb, ASTNode node,
      StringBuilder sb, int depth) {
    sb.append(indent(depth)).append(node.getClass().getSimpleName()).append("| ");
    HashMap<String, String> attributes = new HashMap<String, String>();
    int startLine = unit.getLineNumber(node.getStartPosition());
    int startColumn = unit.getColumnNumber(node.getStartPosition());
    int endLine = startLine + StringUtils.countMatches(node.toString().trim(), "\n");
    int endColumn = startColumn + node.getLength();
    attributes.put(StartLineName, String.valueOf(startLine));
    attributes.put(StartPositionName, String.valueOf(startColumn));
    attributes.put(EndLineName, String.valueOf(endLine));
    attributes.put(EndPositionName, String.valueOf(endColumn));
    xb.addNode(node, depth, attributes);

    List<StructuralPropertyDescriptor> structualProperties = node.structuralPropertiesForType();
    String delimiter = "";
    for (StructuralPropertyDescriptor desc : structualProperties) {
      sb.append(delimiter).append(desc.getId()).append(": ");
      if (desc.isSimpleProperty()) {
        Object property = node.getStructuralProperty(desc);
        if (property == null) continue;
        //System.out.println(property.toString() + " : " + ((SimplePropertyDescriptor)desc).getValueType().toString());
        //if (((SimplePropertyDescriptor)desc).getValueType() != String.class) continue;
        sb.append(property).append("\n");
        xb.addProperty(desc.getId(), property.toString(), depth + 1, attributes);
      } else if (desc.isChildProperty()) {
        ASTNode childNode = (ASTNode) node.getStructuralProperty(desc);
        sb.append("\n");
        if (childNode != null) {
          scan(unit, xb, childNode, sb, depth + 1);
        }
      } else if (desc.isChildListProperty()) {
        List<ASTNode> childNodes = (List<ASTNode>) node.getStructuralProperty(desc);
        sb.append("\n");
        if (childNodes != null) {
          for (ASTNode childNode : childNodes) {
            scan(unit, xb, childNode, sb, depth + 1);
          }
        }
      }
      delimiter = indent(depth + 1);
    }
    xb.closeNode(node.getClass().getSimpleName(), depth);
  }
}
