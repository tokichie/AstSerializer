/**
 * Created by tokitake on 2015/07/23.
 */

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

import java.util.List;

public class MyVisitor extends ASTVisitor {

  @Override
  public void preVisit(ASTNode node) {
    StringBuffer sb = new StringBuffer();

    //Modifiers
    //sb.append(StringUtils.join(node.modifiers(), " "));
    sb.append(node.getNodeType());
    sb.append(" ");
    List<StructuralPropertyDescriptor> structualProperties = node.structuralPropertiesForType();
    for (StructuralPropertyDescriptor descriptor: structualProperties) {
      if (descriptor.isSimpleProperty()) {
        sb.append(node.getStructuralProperty(descriptor));
        sb.append(" ");
      }/* else if (descriptor.isChildProperty()) {
        ASTNode childNode = (ASTNode)node.getStructuralProperty(descriptor);
        if (childNode != null) {
          childNode.accept(this);
        }
      } else if (descriptor.isChildListProperty()) {
        List<ASTNode> childNodes = (List<ASTNode>)node.getStructuralProperty(descriptor);
        if (childNodes != null) {
          for (ASTNode childNode : childNodes) {
            childNode.accept(this);
          }
        }
      }*/
    }

    switch (node.getNodeType()) {
      case ASTNode.ANNOTATION_TYPE_DECLARATION:
        break;
      case ASTNode.ANNOTATION_TYPE_MEMBER_DECLARATION:
        break;
    }

    // Return Type
    //if( !node.isConstructor() ){
      //sb.append(node.getReturnType2().toString() );
      //sb.append(" ");
    //}

    // Parameters
    //sb.append(node.getName().toString());
    //sb.append("(");
    //sb.append(StringUtils.join(node.parameters(), ", "));
    //sb.append(")");

    System.out.println(sb);
  }
}
