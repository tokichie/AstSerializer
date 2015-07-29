/**
 * Created by tokitake on 2015/07/23.
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

public class AstTest {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("File is not defined.");
			System.exit(1);
		}
		try {
			System.out.println("Reading " + args[0] + " . . .");
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(args[0])));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}

			// Create AST Parser
			ASTParser parser = ASTParser.newParser(AST.JLS3);
			parser.setSource(sb.toString().toCharArray());
			CompilationUnit unit = (CompilationUnit) parser
					.createAST(new NullProgressMonitor());

			StringBuilder out = new StringBuilder();
			scan(unit, out, 0);
			System.out.println(out);

			System.out.println("Done !");
		} catch (FileNotFoundException e) {
			System.out.println("File " + args[0] + " not found.");
			return;
		} catch (IOException e) {
			System.out.println("IO Exception !");
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

	private static void scan(ASTNode node, StringBuilder sb, int depth) {
		sb.append(indent(depth)).append(node.getClass().getSimpleName())
				.append("| ");

		List<StructuralPropertyDescriptor> structualProperties = node
				.structuralPropertiesForType();
		String delimiter = "";
		for (StructuralPropertyDescriptor desc : structualProperties) {
			sb.append(delimiter).append(desc.getId()).append(": ");
			if (desc.isSimpleProperty()) {
				sb.append(node.getStructuralProperty(desc)).append("\n");
			} else if (desc.isChildProperty()) {
				ASTNode childNode = (ASTNode) node.getStructuralProperty(desc);
				sb.append("\n");
				if (childNode != null) {
					scan(childNode, sb, depth + 2);
				}
			} else if (desc.isChildListProperty()) {
				List<ASTNode> childNodes = (List<ASTNode>) node
						.getStructuralProperty(desc);
				sb.append("\n");
				if (childNodes != null) {
					for (ASTNode childNode : childNodes) {
						scan(childNode, sb, depth + 2);
					}
				}
			}
			delimiter = indent(depth + 1);
		}
	}
}
