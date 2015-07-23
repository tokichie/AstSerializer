/**
 * Created by tokitake on 2015/07/23.
 */
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class AstTest {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("File is not defined.");
      System.exit(1);
    }
    try {
      System.out.println("Reading " + args[0] + " . . .");
      StringBuffer sb = new StringBuffer();
      BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream(args[0])));
      String line;
      while( (line = br.readLine()) != null ){
        sb.append(line+"\n");
      }

      // Create AST Parser
      ASTParser parser = ASTParser.newParser(AST.JLS3);
      parser.setSource(sb.toString().toCharArray());
      CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());

      // Visit Node
      MyVisitor visitor = new MyVisitor();
      unit.accept(visitor);

      System.out.println("Done !");
    } catch ( FileNotFoundException e) {
      System.out.println("File " + args[0] + " not found.");
      return;
    } catch ( IOException e ){
      System.out.println("IO Exception !");
      return;
    }
  }
}
