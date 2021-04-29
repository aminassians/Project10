package nand2P10;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CompilationEngine {
    FileWriter fileWriter;
    JackTokenizer tokenizer;
    boolean bool;

    public CompilationEngine(File fileIn, File fileOut) 
    {
        try 
        {
            fileWriter = new FileWriter(fileOut);
            tokenizer = new JackTokenizer(fileIn);
        } 
        catch (IOException exception) 
        {
        	exception.printStackTrace();
        }
        bool = true;
    }

    public void compileClass() 
    {
        try {
            tokenizer.advance();
            fileWriter.write("<class>\n");
            fileWriter.write("<keyword> class </keyword>\n");
            
            tokenizer.advance();
            fileWriter.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
            
            tokenizer.advance();
            fileWriter.write("<symbol> { </symbol>\n");
            compileClassVarDec();
            compileSubRoutine();
            fileWriter.write("<symbol> } </symbol>\n");
            fileWriter.write("</class>\n");
            
            fileWriter.close();
        } 
        catch (IOException exception)
        {
        	exception.printStackTrace();
        }

    }

    public void compileClassVarDec()
    {
        tokenizer.advance();
        try {
            while (tokenizer.keyWord().equals("static") || tokenizer.keyWord().equals("field")) 
            {
                fileWriter.write("<classVarDec>\n");
                fileWriter.write("<keyword> " + tokenizer.keyWord() + " </keyword>\n");
                tokenizer.advance();
                
                if (tokenizer.tokenType().equals("IDENTIFIER"))
                {
                    fileWriter.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
                }
                else 
                {
                    fileWriter.write("<keyword> " + tokenizer.keyWord() + "</keyword>\n");

                }
                tokenizer.advance();
                fileWriter.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
                tokenizer.advance();
                
                if (tokenizer.symbol() == ',') 
                {
                    fileWriter.write("<symbol> , </symbol>\n");
                    tokenizer.advance();
                    fileWriter.write(("<identifier> " + tokenizer.identifier() + "</identifier>\n"));
                    tokenizer.advance();
                }
                fileWriter.write("<symbol> ; </symbol>\n");
                tokenizer.advance();
                fileWriter.write("</classVarDec>\n");
            }

            if (tokenizer.keyWord().equals("function") || tokenizer.keyWord().equals("method") || tokenizer.keyWord().equals("constructor")) {
                tokenizer.decrementPointer();
                return;
            }

        } catch (IOException exception) {
        	exception.printStackTrace();
        }


    }

    public void compileSubRoutine() {
        boolean hasSubRoutines = false;

        tokenizer.advance();
        try {
            if (tokenizer.symbol() == '}' && tokenizer.tokenType().equals("SYMBOL")) {
                return;
            }
            if ((bool) && (tokenizer.keyWord().equals("function") || tokenizer.keyWord().equals("method") || tokenizer.keyWord().equals("constructor"))) {
                bool = false;
                fileWriter.write("<subroutineDec>\n");
                hasSubRoutines = true;
            }
            if (tokenizer.keyWord().equals("function") || tokenizer.keyWord().equals("method") || tokenizer.keyWord().equals("constructor")) {
                hasSubRoutines = true;
                fileWriter.write("<keyword> " + tokenizer.keyWord() + " </keyword>\n");
                tokenizer.advance();
            }
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                fileWriter.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
                tokenizer.advance();
            }
            else if (tokenizer.tokenType().equals("KEYWORD")) {
                fileWriter.write("<keyword> " + tokenizer.keyWord() + "</keyword>\n");
                tokenizer.advance();
            }
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                fileWriter.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
                tokenizer.advance();
            }
            if (tokenizer.symbol() == '(') {
                fileWriter.write("<symbol> ( </symbol>\n");
                fileWriter.write("<parameterList>\n");

                compileParameterList();
                fileWriter.write("</parameterList>\n");
                fileWriter.write("<symbol> ) </symbol>\n");

            }
            tokenizer.advance();
            if (tokenizer.symbol() == '{') {
                fileWriter.write("<subroutineBody>\n");
                fileWriter.write("<symbol> { </symbol>\n");
                tokenizer.advance();
            }
            while (tokenizer.keyWord().equals("var") && (tokenizer.tokenType().equals("KEYWORD"))) {
                fileWriter.write("<varDec>\n ");
                tokenizer.decrementPointer();
                compileVarDec();
                fileWriter.write(" </varDec>\n");
            }
            fileWriter.write("<statements>\n");
            compileStatements();
            fileWriter.write("</statements>\n");
            fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
            if (hasSubRoutines) {
                fileWriter.write("</subroutineBody>\n");
                fileWriter.write("</subroutineDec>\n");
                bool = true;
            }

            compileSubRoutine();

        } catch (IOException exception) {
        	exception.printStackTrace();
        }

    }

    public void compileParameterList() {
        tokenizer.advance();
        try {
            while (!(tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ')')) {
                if (tokenizer.tokenType().equals("IDENTIFIER")) {
                    fileWriter.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
                    tokenizer.advance();
                } else if (tokenizer.tokenType().equals("KEYWORD")) {
                    fileWriter.write("<keyword> " + tokenizer.keyWord() + "</keyword>\n");
                    tokenizer.advance();
                }
                else if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == ',')) {
                    fileWriter.write("<symbol> , </symbol>\n");
                    tokenizer.advance();

                }
            }
        } catch (IOException exception) {
        	exception.printStackTrace();
        }
    }

    public void compileVarDec() {
        tokenizer.advance();
        try {

            if (tokenizer.keyWord().equals("var") && (tokenizer.tokenType().equals("KEYWORD"))) {
                fileWriter.write("<keyword> var </keyword>\n");
                tokenizer.advance();
            }
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                fileWriter.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
                tokenizer.advance();
            }
            else if (tokenizer.tokenType().equals("KEYWORD")) {
                fileWriter.write("<keyword> " + tokenizer.keyWord() + " </keyword>\n");
                tokenizer.advance();
            }
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                fileWriter.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
                tokenizer.advance();
            }
            if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == ',')) {
                fileWriter.write("<symbol> , </symbol>\n");
                tokenizer.advance();
                fileWriter.write(("<identifier> " + tokenizer.identifier() + "</identifier>\n"));
                tokenizer.advance();
            }
            if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == ';')) {
                fileWriter.write("<symbol> ; </symbol>\n");
                tokenizer.advance();

            }
        } catch (IOException exception) {
        	exception.printStackTrace();
        }

    }

    public void compileStatements() {
        try {
            if (tokenizer.symbol() == '}' && (tokenizer.tokenType().equals("SYMBOL"))) {
                return;
            } else if (tokenizer.keyWord().equals("do") && (tokenizer.tokenType().equals("KEYWORD"))) {
                fileWriter.write("<doStatement>\n ");
                compileDo();
                fileWriter.write((" </doStatement>\n"));

            } else if (tokenizer.keyWord().equals("let") && (tokenizer.tokenType().equals("KEYWORD"))) {
                fileWriter.write("<letStatement>\n ");
                compileLet();
                fileWriter.write((" </letStatement>\n"));
            } else if (tokenizer.keyWord().equals("if") && (tokenizer.tokenType().equals("KEYWORD"))) {
                fileWriter.write("<ifStatement>\n ");
                compileIf();
                fileWriter.write((" </ifStatement>\n"));
            } else if (tokenizer.keyWord().equals("while") && (tokenizer.tokenType().equals("KEYWORD"))) {
                fileWriter.write("<whileStatement>\n ");
                compileWhile();
                fileWriter.write((" </whileStatement>\n"));
            } else if (tokenizer.keyWord().equals("return") && (tokenizer.tokenType().equals("KEYWORD"))) {
                fileWriter.write("<returnStatement>\n ");
                compileReturn();
                fileWriter.write((" </returnStatement>\n"));
            }
            tokenizer.advance();
            compileStatements();
        } catch (IOException exception) {
        	exception.printStackTrace();
        }

    }

    public void compileDo() {
        try {
            if (tokenizer.keyWord().equals("do")) {
                fileWriter.write("<keyword> do </keyword>\n");
            }
            compileCall();
            tokenizer.advance();
            fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");


        } catch (IOException exception) {
        	exception.printStackTrace();

        }
    }

    private void compileCall() {
        tokenizer.advance();
        try {
            fileWriter.write("<identifier> " + tokenizer.identifier() + "</identifier>\n");
            tokenizer.advance();
            if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == '.')) {
                fileWriter.write("<symbol> " + tokenizer.symbol() + "</symbol>\n");
                tokenizer.advance();
                fileWriter.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
                tokenizer.advance();
                fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                fileWriter.write("<expressionList>\n");
                compileExpressionList();
                fileWriter.write("</expressionList>\n");
                tokenizer.advance();
                fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");


            }
            else if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == '(')) {
                fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                fileWriter.write("<expressionList>\n");
                compileExpressionList();
                fileWriter.write("</expressionList>\n");
                tokenizer.advance();
                fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");


            }
        } catch (IOException exception) {
        	exception.printStackTrace();
        }
    }

    public void compileLet() {
        try {
            fileWriter.write("<keyword>" + tokenizer.keyWord() + "</keyword>\n");
            tokenizer.advance();
            fileWriter.write("<identifier> " + tokenizer.identifier() + " </identifier>\n");
            tokenizer.advance();
            if ((tokenizer.tokenType().equals("SYMBOL")) && (tokenizer.symbol() == '[')) {
                fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                compileExpression();
                tokenizer.advance();
                if ((tokenizer.tokenType().equals("SYMBOL")) && ((tokenizer.symbol() == ']'))) {
                    fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                }
                tokenizer.advance();

            }

            fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");

            compileExpression();
            fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
            tokenizer.advance();
        } catch (IOException exception) {
        	exception.printStackTrace();
        }
    }

    public void compileWhile() {
        try {
            fileWriter.write("<keyword>" + tokenizer.keyWord() + "</keyword>\n");
            tokenizer.advance();
            
            fileWriter.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");
            compileExpression();
            
            tokenizer.advance();
            fileWriter.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");
            tokenizer.advance();
            
            fileWriter.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");
            
            fileWriter.write("<statements>\n");
            compileStatements();
            fileWriter.write("</statements>\n");
            
            fileWriter.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");

        } catch (IOException exception) {
        	exception.printStackTrace();
        }
    }

    public void compileReturn() {
        try {
            fileWriter.write("<keyword> return </keyword>\n");
            tokenizer.advance();
            if (!((tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ';'))) {
                tokenizer.decrementPointer();
                compileExpression();
            }
            if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ';') {
                fileWriter.write("<symbol> ; </symbol>\n");
            }
        } catch (IOException exception) {
        	exception.printStackTrace();
        }


    }

    public void compileIf() {
        try {
            fileWriter.write("<keyword> if </keyword>\n");
            tokenizer.advance();
            fileWriter.write("<symbol> ( </symbol>\n");
           
            compileExpression();
            fileWriter.write("<symbol> ) </symbol>\n");
            tokenizer.advance();
            fileWriter.write("<symbol> { </symbol>\n");
            tokenizer.advance();
            fileWriter.write("<statements>\n");
            
            compileStatements();
            fileWriter.write("</statements>\n");
            fileWriter.write("<symbol> } </symbol>\n");
            tokenizer.advance();
           
            if (tokenizer.tokenType().equals("KEYWORD") && tokenizer.keyWord().equals("else")) {
                fileWriter.write("<keyword> else </keyword>\n");
                tokenizer.advance();
                fileWriter.write("<symbol> { </symbol>\n");
                tokenizer.advance();
                fileWriter.write("<statements>\n");
                
                compileStatements();
                fileWriter.write("</statements>\n");
                fileWriter.write("<symbol> } </symbol>\n");
            } else {
                
                tokenizer.decrementPointer();
            }
        } catch (IOException exception) {
        	exception.printStackTrace();
        }


    }

    
    public void compileExpression() {
        try {
            fileWriter.write("<expression>\n");
            compileTerm();
            while (true) {
                tokenizer.advance();
                if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.isOperation()) {
                    
                    if (tokenizer.symbol() == '<') {
                        fileWriter.write("<symbol> &lt; </symbol>\n");
                    } else if (tokenizer.symbol() == '>') {
                        fileWriter.write("<symbol> &gt; </symbol>\n");
                    } else if (tokenizer.symbol() == '&') {
                        fileWriter.write("<symbol> &amp; </symbol>\n");
                    } else {
                        fileWriter.write("<symbol> " + tokenizer.symbol() + " </symbol>\n");
                    }
                    compileTerm();
                } else {
                    tokenizer.decrementPointer();
                    break;
                }
            }
            fileWriter.write("</expression>\n");


        } catch (IOException exception) {
        	exception.printStackTrace();
        }
    }

    public void compileTerm() {
        try {
            fileWriter.write("<term>\n");
            tokenizer.advance();
            if (tokenizer.tokenType().equals("IDENTIFIER")) {
                String prevIdentifier = tokenizer.identifier();
                tokenizer.advance();
               
                if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == '[') {
                    fileWriter.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    fileWriter.write("<symbol> [ </symbol>\n");
                    compileExpression();
                    tokenizer.advance();
                    fileWriter.write("<symbol> ] </symbol>\n");
                }
                
                else if (tokenizer.tokenType().equals("SYMBOL") && (tokenizer.symbol() == '(' || tokenizer.symbol() == '.')) {
                    tokenizer.decrementPointer();
                    tokenizer.decrementPointer();
                    compileCall();

                } else {
                    fileWriter.write("<identifier> " + prevIdentifier + " </identifier>\n");
                    tokenizer.decrementPointer();
                }
            } else {
                if (tokenizer.tokenType().equals("INT_CONST")) {
                    fileWriter.write("<integerConstant> " + tokenizer.intVal() + " </integerConstant>\n");

                }
                else if (tokenizer.tokenType().equals("STRING_CONST")) {
                    fileWriter.write("<stringConstant> " + tokenizer.stringVal() + " </stringConstant>\n");
                }
                else if (tokenizer.tokenType().equals("KEYWORD") && (tokenizer.keyWord().equals("this") || tokenizer.keyWord().equals("null")
                        || tokenizer.keyWord().equals("false") || tokenizer.keyWord().equals("true"))) {
                    fileWriter.write("<keyword> " + tokenizer.keyWord() + " </keyword>\n");
                }
                else if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == '(') {
                    fileWriter.write("<symbol>" + tokenizer.symbol() + "</symbol>\n");
                    compileExpression();
                    tokenizer.advance();
                    fileWriter.write("<symbol> " + tokenizer.symbol() + "</symbol>\n");
                }
                else if (tokenizer.tokenType().equals("SYMBOL") && (tokenizer.symbol() == '-' || tokenizer.symbol() == '~')) {
                    fileWriter.write("<symbol> " + tokenizer.symbol() + "</symbol>\n");
                    compileTerm();
                }
            }
            fileWriter.write("</term>\n");

        } catch (IOException exception) {
        	exception.printStackTrace();
        }
    }

    public void compileExpressionList() {
        tokenizer.advance();
        
        if (tokenizer.symbol() == ')' && tokenizer.tokenType().equals("SYMBOL")) {
            tokenizer.decrementPointer();
        } else {
            tokenizer.decrementPointer();
            compileExpression();
        }
        while (true) {
            tokenizer.advance();
            if (tokenizer.tokenType().equals("SYMBOL") && tokenizer.symbol() == ',') {
                try {
                    fileWriter.write("<symbol> , </symbol>\n");
                } catch (IOException exception) {
                	exception.printStackTrace();
                }
                compileExpression();
            } else {
                tokenizer.decrementPointer();
                break;
            }
        }

    }
}