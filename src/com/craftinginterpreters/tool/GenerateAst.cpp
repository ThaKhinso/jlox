#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <filesystem>
#include <sstream>

using namespace std;
namespace fs = std::filesystem;

void defineAst(const string& outputDir, const string& baseName, const vector<string>& types);
void defineType(ofstream& writer, const string& baseName, const string& className, const string& fieldList);
void defineVisitor(ofstream& writer, const string& baseName, const vector<string>& types);

int main() {
    string outputDir = "C:\\dev\\javaproj\\jlox\\src\\com\\craftinginterpreters\\lox";

    defineAst(outputDir, "Expr", {
        "Assign   : Token name, Expr value",
        "Binary   : Expr left, Token operator, Expr right",
        "Grouping : Expr expression",
        "Literal  : Object value",
        "Unary    : Token operator, Expr right",
        "Variable : Token name"
    });

    defineAst(outputDir, "Stmt", {
        "Block      : List<Stmt> statements",
        "Expression : Expr expression",
        "If         : Expr condition, Stmt thenBranch, Stmt else_Branch",
        "Print      : Expr expression",
        "Var        : Token name, Expr initializer"
    });

    return 0;
}

void defineAst(const string& outputDir, const string& baseName, const vector<string>& types) {
    fs::create_directories(outputDir);
    string path = outputDir + "\\" + baseName + ".java";
    ofstream writer(path);

    writer << "package com.craftinginterpreters.lox;\n\n";
    writer << "import java.util.List;\n\n";
    writer << "abstract class " << baseName << " {\n\n";

    defineVisitor(writer, baseName, types);

    // Define each AST type
    for (const string& type : types) {
        size_t colon = type.find(':');
        string className = type.substr(0, colon);
        className.erase(remove(className.begin(), className.end(), ' '), className.end());
        string fields = type.substr(colon + 1);
        fields.erase(0, fields.find_first_not_of(" "));
        defineType(writer, baseName, className, fields);
    }

    // Base accept method
    writer << "\n    abstract <R> R accept(Visitor<R> visitor);\n";
    writer << "}\n";
    writer.close();
}

void defineType(ofstream& writer, const string& baseName, const string& className, const string& fieldList) {
    writer << "\n    static class " << className << " extends " << baseName << " {\n";

    // Constructor
    writer << "        " << className << "(" << fieldList << ") {\n";
    stringstream ss(fieldList);
    string field;
    while (getline(ss, field, ',')) {
        field.erase(0, field.find_first_not_of(' '));
        size_t space = field.find(' ');
        string name = field.substr(space + 1);
        writer << "            this." << name << " = " << name << ";\n";
    }
    writer << "        }\n";

    // Visitor pattern accept
    writer << "\n        @Override\n";
    writer << "        <R> R accept(Visitor<R> visitor) {\n";
    writer << "            return visitor.visit" << className << baseName << "(this);\n";
    writer << "        }\n";

    // Fields
    writer << "\n";
    ss.clear();
    ss.str(fieldList);
    while (getline(ss, field, ',')) {
        field.erase(0, field.find_first_not_of(' '));
        writer << "        final " << field << ";\n";
    }

    writer << "    }\n";
}

void defineVisitor(ofstream& writer, const string& baseName, const vector<string>& types) {
    writer << "    interface Visitor<R> {\n";

    for (const string& type : types) {
        size_t colon = type.find(':');
        string typeName = type.substr(0, colon);
        typeName.erase(remove(typeName.begin(), typeName.end(), ' '), typeName.end());
        writer << "        R visit" << typeName << baseName << "(" << typeName << " " << baseName << ");\n";
    }

    writer << "    }\n";
}
