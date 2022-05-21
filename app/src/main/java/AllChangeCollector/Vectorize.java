package AllChangeCollector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class Vectorize {
    static HashMap<String, Integer> map = new HashMap<>();
    static ArrayList<Integer> astType = new ArrayList<>();
    
    public static void extract_vector(String repo_name) throws FileNotFoundException, IOException {

        // reading change file extracted by gumtree
        String dir = System.getProperty("user.dir") + "/data/" + repo_name + "/gumtree_log.txt";
        File file = new File(dir);

        // create diretory to save vector files
        File directory_vector = new File(System.getProperty("user.dir") + "/vector");
        File vector_file = null;

        if (!directory_vector.exists()) {
            directory_vector.mkdir();
        }

        String vector_file_name = directory_vector.toString();
        vector_file = new File(vector_file_name, repo_name + "_gumtree_vector.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(vector_file, true));
        String write_line = "";

        // reading file
        boolean no_change = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;

            // read each line
            boolean add = false;
            int oper = 0;
            // boolean hash = false;    

            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                // examine tokens and write for matching string
                while (st.hasMoreTokens()) {
                    String st_l = st.nextToken();

                    if (st_l.equals("[]")) {
                        no_change = true;
                    }
                    if (st_l.matches("insert-node|delete-node|update-node|insert-tree|delete-tree|move-tree")) {
                        // writing
                        if (astType.size() > 0 && oper != -1) { // FIX: remove ',' on each line
                            for (int i = 0; i < astType.size(); i++) {
                                int val = 170 * oper + astType.get(i); //vector
                                write_line += val + ",";
                            }
                        }
                        astType.clear();
                        oper = decide_node(st_l);
                    }

                    if (st_l.equals("---")) {
                        add = true;
                    }
                    if (st_l.equals("===")) {
                        add = false;
                    }
                    /*
                    deleted, reason: hashcode wasn't needed for the vectorizing
                    if (hash) {
                        if (map.containsKey(st_l)) {
                            String code = map.get(st_l).toString();
                            write_line += code + ", ";
                        } else {
                            int hash_num = st_l.hashCode();
                            map.put(st_l, hash_num);
                            write_line += hash_num + ", ";
                        }
                    
                        hash = false;
                    }
                    */

                    if (add == true) {
                        if ((!Character.isAlphabetic(st_l.charAt(st_l.length() - 1))) && add) {
                            st_l = st_l.substring(0, st_l.length() - 1);
                        }

                        for (int i = 0; i < expanded_node.length; i++) {
                            if (st_l.equals(expanded_node[i])) {
                                astType.add(i + 1);
                            }
                        }
                    }
                }
            }
            writer.write(write_line + '\n');
        }
        
        writer.close();
    }
    
    public static void extract_vector_lec(String repo_name) throws FileNotFoundException, IOException {

        // reading change file extracted by gumtree
        System.out.println("======>Vectorizing");
        String dir = System.getProperty("user.dir") + "/lec/" + repo_name + "/gumtree_lec_log.txt";
        File file = new File(dir);

        // create diretory to save vector files
        File directory_vector = new File(System.getProperty("user.dir") + "/vector");
        File vector_file = null;

        if (!directory_vector.exists()) {
            directory_vector.mkdir();
        }

        String vector_file_name = directory_vector.toString();
        vector_file = new File(vector_file_name, repo_name + "_gumtree_vector.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(vector_file, true));
        String write_line = "";

        // reading file
        boolean no_change = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;

            // read each line
            boolean add = false;
            int oper = 0;
            // boolean hash = false;

            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                // examine tokens and write for matching string
                while (st.hasMoreTokens()) {
                    String st_l = st.nextToken();

                    if (st_l.equals("[]")) {
                        no_change = true;
                    }
                    if (st_l.matches("insert-node|delete-node|update-node|insert-tree|delete-tree|move-tree")) {
                        // writing
                        if (astType.size() > 0 && oper != -1) { // FIX: remove ',' on each line
                            for (int i = 0; i < astType.size(); i++) {
                                int val = 170 * oper + astType.get(i); // vector
                                write_line += val + ",";
                            }
                        }
                        astType.clear();
                        oper = decide_node(st_l);
                    }

                    if (st_l.equals("---")) {
                        add = true;
                    }
                    if (st_l.equals("===")) {
                        add = false;
                    }
                    /*
                     * deleted, reason: hashcode wasn't needed for the vectorizing
                     * if (hash) {
                     * if (map.containsKey(st_l)) {
                     * String code = map.get(st_l).toString();
                     * write_line += code + ", ";
                     * } else {
                     * int hash_num = st_l.hashCode();
                     * map.put(st_l, hash_num);
                     * write_line += hash_num + ", ";
                     * }
                     * 
                     * hash = false;
                     * }
                     */

                    if (add == true) {
                        if ((!Character.isAlphabetic(st_l.charAt(st_l.length() - 1))) && add) {
                            st_l = st_l.substring(0, st_l.length() - 1);
                        }

                        for (int i = 0; i < expanded_node.length; i++) {
                            if (st_l.equals(expanded_node[i])) {
                                astType.add(i + 1);
                            }
                        }
                    }
                }
            }
            writer.write(write_line + '\n');
        }

        writer.close();
    }


    public static void extract_vector_csv(String repo_name) throws FileNotFoundException, IOException {
        System.out.println("=====> Parsing");
        System.out.println("===========> " + repo_name);

        // reading change file extracted by gumtree
        String dir = System.getProperty("user.dir") + "/data/" + repo_name + "/gumtree_log.txt";
        File file = new File(dir);

        // create diretory to save vector files
        File directory_vector = new File(System.getProperty("user.dir") + "/vector");
        File vector_file = null;

        if (!directory_vector.exists()) {
            directory_vector.mkdir();
        }

        String vector_file_name = directory_vector.toString();
        vector_file = new File(vector_file_name, repo_name + "_gumtree_vector.csv");
        //BufferedWriter writer = new BufferedWriter(new FileWriter(vector_file));
        try (PrintWriter writer = new PrintWriter(new FileWriter(vector_file))) {
            StringBuilder sb = new StringBuilder();

            // reading file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = null;

                // read each line
                boolean add = false;
                boolean hash = false;
                while ((line = reader.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line);

                    // examine tokens and write for matching string
                    while (st.hasMoreTokens()) {
                        String st_l = st.nextToken();

                        if (st_l.matches("insert-node|delete-node|update-node|insert-tree|delete-tree|move-tree")) {
                            writer.write(sb.toString() + "\n");
                            sb.setLength(0);
                        }
                        sb.append(decide_node(st_l));

                        if (st_l.equals("---")) {
                            add = true;
                        }
                        if (st_l.equals("===")) {
                            add = false;
                        }
                        if (hash) {
                            if (map.containsKey(st_l)) {
                                String code = map.get(st_l).toString();
                                sb.append(code + ",");
                            } else {
                                int hash_num = st_l.hashCode();
                                map.put(st_l, hash_num);
                                sb.append(hash_num + ",");
                            }

                            hash = false;
                        }

                        if (add == true) {
                            if ((!Character.isAlphabetic(st_l.charAt(st_l.length() - 1))) && add) {
                                st_l = st_l.substring(0, st_l.length() - 1);
                            }

                            for (int i = 0; i < expanded_node.length; i++) {
                                if (st_l.equals(expanded_node[i])) {
                                    sb.append(String.valueOf(i + 1) + ",");
                                    if (st_l.equals("SimpleName")) {
                                        hash = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            writer.write(sb.toString() + "\n");
        }
    }

    public static int decide_node(String str)
    {
        // on node types
        if (str.equals("delete-node")) {
            
            return 0;
        }
        if (str.equals("insert-node")) {
            return 1;
        }
        if (str.equals("update-node")) {
            return 2;
        }

        // on tree types
        if (str.equals("delete-tree")) {
            return 3;
        }
        if (str.equals("insert-tree")) {
            return 4;
        }
        if (str.equals("move-tree")) {
            return 5;
        }

        System.out.println("Debug: " + str); // DEBUG
        return -1;
    }
    
    static public String[] nodes = {
            /* 4. */ "AnnotationTypeDeclaration",
            /* 5. */ "AnnotationTypeMemberDeclaration",
            /* 6. */ "AnonymousClassDeclaration",
            /* 7. */ "ArrayAccess",
            /* 8. */ "ArrayCreation",
            /* 9. */ "ArrayInitializer",
            /* 10. */ "ArrayType",
            /* 11. */ "AssertStatement",
            /* 12. */ "Assignment",
            /* 13. */ "Block",
            /* 14. */ "BlockComment",
            /* 15. */ "BooleanLiteral",
            /* 16. */ "BreakStatement",
            /* 17. */ "CastExpression",
            /* 18. */ "CatchClause",
            /* 19. */ "CharacterLiteral",
            /* 20. */ "ClassInstanceCreation",
            /* 21. */ "CompilationUnit",
            /* 22. */ "ConditionalExpression",
            /* 23. */ "ConstructorInvocation",
            /* 24. */ "ContinueStatement",
            /* 25. */ "CreationReference",
            /* 26. */ "Dimension",
            /* 27. */ "DoStatement",
            /* 28. */ "EmptyStatement",
            /* 29. */ "EnhancedForStatement",
            /* 30. */ "EnumConstantDeclaration",
            /* 31. */ "EnumDeclaration",
            /* 32. */ "ExportsDirective",
            /* 33. */ "ExpressionMethodReference",
            /* 34. */ "ExpressionStatement",
            /* 35. */ "FieldAccess",
            /* 36. */ "FieldDeclaration",
            /* 37. */ "ForStatement",
            /* 38. */ "IfStatement",
            /* 39. */ "ImportDeclaration",
            /* 40. */ "InfixExpression",
            /* 41. */ "Initializer",
            /* 42. */ "InstanceofExpression",
            /* 43. */ "IntersectionType",
            /* 44. */ "Javadoc",
            /* 45. */ "LabeledStatement",
            /* 46. */ "LambdaExpression",
            /* 47. */ "LineComment",
            /* 48. */ "MarkerAnnotation",
            /* 49. */ "MemberRef",
            /* 50. */ "MemberValuePair",
            /* 51. */ "MethodDeclaration",
            /* 52. */ "MethodInvocation",
            /* 53. */ "MethodRef",
            /* 54. */ "MethodRefParameter",
            /* 55. */ "Modifier",
            /* 56. */ "ModuleDeclaration",
            /* 57. */ "ModuleModifier",
            /* 58. */ "NameQualifiedType",
            /* 59. */ "NormalAnnotation",
            /* 60. */ "NullLiteral",
            /* 61. */ "NumberLiteral",
            /* 62. */ "OpensDirective",
            /* 63. */ "PackageDeclaration",
            /* 64. */ "ParameterizedType",
            /* 65. */ "ParenthesizedExpression",
            /* 66. */ "PostfixExpression",
            /* 67. */ "PrefixExpression",
            /* 68. */ "PrimitiveType",
            /* 69. */ "ProvidesDirective",
            /* 70. */ "QualifiedName",
            /* 71. */ "QualifiedType",
            /* 72. */ "RequiresDirective",
            /* 73. */ "Statement",
            /* 74. */ "SimpleName",
            /* 75. */ "SimpleType",
            /* 76. */ "SingleMemberAnnotation",
            /* 77. */ "SingleVariableDeclaration",
            /* 78. */ "StringLiteral",
            /* 79. */ "SuperConstructorInvocation",
            /* 80. */ "SuperFieldAccess",
            /* 81. */ "SuperMethodInvocation",
            /* 82. */ "SuperMethodReference",
            /* 83. */ "SwitchCase",
            /* 84. */ "SwitchStatement",
            /* 85. */ "SynchronizedStatement",
            /* 86. */ "TagElement",
            /* 87. */ "TextElement",
            /* 88. */ "ThisExpression",
            /* 89. */ "ThrowStatement",
            /* 90. */ "TryStatement",
            /* 91. */ "TypeDeclaration",
            /* 92. */ "TypeDeclarationStatement",
            /* 93. */ "TypeMethodReference",
            /* 94. */ "TypeLiteral",
            /* 95. */ "TypeParameter",
            /* 96. */ "UnionType",
            /* 97. */ "UsesDirective",
            /* 98. */ "VariableDeclarationExpression",
            /* 99. */ "VariableDeclarationFragment",
            /* 100. */ "VariableDeclarationStatement",
            /* 101. */ "WhileStatement",
            /* 102. */ "WildcardType",
            /* 103. */ "INFIX_EXPRESSION_OPERATOR",
            /* 104. */ "METHOD_INVOCATION_RECEIVER",
            /* 105. */ "METHOD_INVOCATION_ARGUMENTS",
            /* 106. */ "TYPE_DECLARATION_KIND",
            /* 107. */ "ASSIGNEMENT_OPERATOR",
            /* 108. */ "PREFIX_EXPRESSION_OPERATOR",
            /* 109. */ "POSTFIX_EXPRESSION_OPERATOR",
            /* 110. */ "ReturnStatement"
    };

    static public String[] expanded_node = {
            /* 1. */ "ABSTRACT",
            /* 2. */ "ANNOTATION_PROPERTY",
            /* 3. */ "AnnotationTypeDeclaration",
            /* 4. */ "AnnotationTypeMemberDeclaration",
            /* 5. */ "ANNOTATIONS_PROPERTY",
            /* 6. */ "AnonymousClassDeclaration",
            /* 7. */ "ANONYMOUS_CLASS_DECLARATION_PROPERTY",
            /* 8. */ "ARGUMENTS_PROPERTY",
            /* 9. */ "ArrayAccess",
            /* 10. */ "ArrayCreation",
            /* 11. */ "ARRAY_PROPERTY",
            /* 12. */ "ArrayType",
            /* 13. */ "ArrayInitializer",
            /* 14. */ "AssertStatement",
            /* 15. */ "Assignment",
            /* 16. */ "Block",
            /* 17. */ "BlockComment",
            /* 18. */ "BODY_DECLARATIONS_PROPERTY",
            /* 19. */ "BODY_PROPERTY",
            /* 20. */ "BooleanLiteral",
            /* 21. */ "BreakStatement",
            /* 22. */ "CastExpression",
            /* 23. */ "CatchClause",
            /* 24. */ "CharacterLiteral",
            /* 25. */ "ClassInstanceCreation",
            /* 26. */ "CompilationUnit",
            /* 27. */ "ConditionalExpression",
            /* 28. */ "ConstructorInvocation",
            /* 29. */ "ContinueStatement",
            /* 30. */ "CreationReference",
            /* 31. */ "DEFAULT",
            /* 32. */ "Dimension",
            /* 33. */ "DIMENSIONS_PROPERTY",
            /* 34. */ "DoStatement",
            /* 35. */ "EmptyStatement",
            /* 36. */ "EnhancedForStatement",
            /* 37. */ "EnumConstantStatement",
            /* 38. */ "EnumDeclaration",
            /* 39. */ "EXCEPTION_PROPERTY",
            /* 40. */ "ExpressionMethodReference",
            /* 41. */ "EXPRESSION_PROPERTY",
            /* 42. */ "ExpressionStatement",
            /* 43. */ "EXTRA_DIMENSIONS2_PROPERTY",
            /* 44. */ "FieldAccess",
            /* 45. */ "FieldDeclaration",
            /* 46. */ "FINAL",
            /* 47. */ "ForStatement",
            /* 48. */ "FRAGMENTS_PROPERTY",
            /* 49. */ "IDENTIFIER_PROPERTY",
            /* 50. */ "IfStatement",
            /* 51. */ "ImportDeclaration",
            /* 52. */ "IMPORT_PROPERTY",
            /* 53. */ "INDEX_PROPERTY",
            /* 54. */ "InfixExpression",
            /* 55. */ "Initializer",
            /* 56. */ "INITIALIZER_PROPERTY",
            /* 57. */ "InstanceofExpression",
            /* 58. */ "IntersactionType",
            /* 59. */ "Javadoc",
            /* 60. */ "JAVADOC_PROPERTY",
            /* 61. */ "KEYWORD_PROPERTY",
            /* 62. */ "LabeledStatement",
            /* 63. */ "LambdaStatement",
            /* 64. */ "LineComment",
            /* 65. */ "MALFORMED",
            /* 66. */ "MarkerAnnotation",
            /* 67. */ "MemberRef",
            /* 68. */ "MemberValuePair",
            /* 69. */ "MethodDeclaration",
            /* 70. */ "MethodInvocation",
            /* 71. */ "MethodRef",
            /* 72. */ "MethodRefParameter",
            /* 73. */ "Modifier",
            /* 74. */ "MODIFIERS_PROPERTY",
            /* 75. */ "MODIFIERS2_PROPERTY",
            /* 76. */ "NAME_PROPERTY",
            /* 77. */ "NameQualifiedType",
            /* 78. */ "NATIVE",
            /* 79. */ "NONE",
            /* 80. */ "NormalAnnotation",
            /* 81. */ "NullLiteral",
            /* 82. */ "NumberLiteral",
            /* 83. */ "ON_DEMAND_PROPERTY",
            /* 84. */ "ORIGINAL",
            /* 85. */ "PackageDeclaration",
            /* 86. */ "PACKAGE_PROPERTY",
            /* 87. */ "ParameterizedType",
            /* 88. */ "PARAMETERS_PROPERTY",
            /* 89. */ "ParenthesizedExpression",
            /* 90. */ "PostfixExpression",
            /* 91. */ "PrefixExpression",
            /* 92. */ "PrimitiveType",
            /* 93. */ "PRIVATE",
            /* 94. */ "PROTECT",
            /* 95. */ "PROTECTED",
            /* 96. */ "PUBLIC",
            /* 97. */ "QualifiedName",
            /* 98. */ "QualifiedType",
            /* 99. */ "QUALIFIER_PROPERTY",
            /* 100. */ "RECOVERED",
            /* 101. */ "ReturnStatement",
            /* 102. */ "SimpleName",
            /* 103. */ "SimpleType",
            /* 104. */ "SingleMemberAnnotation",
            /* 105. */ "SingleVariableDeclaration",
            /* 106. */ "STATIC",
            /* 107. */ "STATIC_PROPERTY",
            /* 108. */ "STRICTFP",
            /* 109. */ "StringLiteral",
            /* 110. */ "SuperConstructorInvocation",
            /* 111. */ "SuperFieldAccess",
            /* 112. */ "SuperMethodInvocation",
            /* 113. */ "SuperMethodReference",
            /* 114. */ "SwitchCase",
            /* 115. */ "SwitchStatement",
            /* 116. */ "SYNCHRONIZED",
            /* 117. */ "SynchronizedStatement",
            /* 118. */ "TAG_AUTHOR",
            /* 119. */ "TAG_CODE",
            /* 120. */ "TAG_DEPRECATED",
            /* 121. */ "TAG_DOCROOT",
            /* 122. */ "TagElement",
            /* 123. */ "TAG_EXCEPTION",
            /* 124. */ "TAG_INHERITDOC",
            /* 125. */ "TAG_LINK",
            /* 126. */ "TAG_LINKPLAIN",
            /* 127. */ "TAG_LITERAL",
            /* 128. */ "TAG_NAME_PROPERTY",
            /* 129. */ "TAG_PARAM",
            /* 130. */ "TAG_RETURN",
            /* 131. */ "TAG_SEE",
            /* 132. */ "TAG_SERIAL",
            /* 133. */ "TAG_SERIALDATA",
            /* 134. */ "TAG_SERIALFIELD",
            /* 135. */ "TAG_SINCE",
            /* 136. */ "TAG_VERSION",
            /* 137. */ "TAGS_PROPERTY",
            /* 138. */ "TextElement",
            /* 139. */ "TEXT_PROPERTY",
            /* 140. */ "ThisExpression",
            /* 141. */ "ThrowExpression",
            /* 142. */ "TRANSIENT",
            /* 143. */ "TryStatement",
            /* 144. */ "TYPE_ARGUMENTS_PROPERTY",
            /* 145. */ "TYPE_BOUNDS_PROPERTY",
            /* 146. */ "TypeDeclaration",
            /* 147. */ "TypeDeclarationStatement",
            /* 148. */ "TypeLiteral",
            /* 149. */ "TypeMethodReference",
            /* 150. */ "TYPE_NAME_PROPERTY",
            /* 151. */ "TypeParameter",
            /* 152. */ "TYPE_PROPERTY",
            /* 153. */ "TYPES_PROPERTY",
            /* 154. */ "UnionType",
            /* 155. */ "VALUE_PROPERTY",
            /* 156. */ "VALUES_PROPERTY",
            /* 157. */ "VARARGS_ANNOTATIONS_PROPERTY",
            /* 158. */ "VARARGS_PROPERTY",
            /* 159. */ "VariableDeclarationExpression",
            /* 160. */ "VariableDeclarationFragment",
            /* 161. */ "VarriableDeclarationStatement",
            /* 162. */ "VOLATILE",
            /* 163. */ "WhileStatement",
            /* 164. */ "WildcardType",
            /* 165. */ "INFIX_EXPRESSION_OPERATOR",
            /* 166. */ "METHOD_INVOCATION_RECEIVER",
            /* 167. */ "METHOD_INVOCATION_ARGUMENTS",
            /* 168. */ "TYPE_DECLARATION_KIND",
            /* 169. */ "ASSIGNEMENT_OPERATOR",
            /* 170. */ "PREFIX_EXPRESSION_OPERATOR",
            /* 171. */ "POSTFIX_EXPRESSION_OPERATOR",
            /* 172. */ "ExportsDirective",
            /* 173. */ "ModuleDeclaration",
            /* 174. */ "ModuleModifier",
            /* 175. */ "OpensDirective",
            /* 176. */ "Statement",
            /* 177. */ "UsesDirective",
    };
}
