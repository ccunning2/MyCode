//
//  main.cpp
//  decoder
//
//  Created by Cameron Cunning on 4/9/15.
//  Copyright (c) 2015 Cameron Cunning. All rights reserved.
//
#include <vector>
#include <iostream>
#include <map>
#include <string>

using namespace std;


/*
 Split method I use in every c++ version of a java program using String.split()
 */

vector<string> split(string str, string sep){
    char* cstr=const_cast<char*>(str.c_str());
    char* current;
    std::vector<std::string> arr;
    current=strtok(cstr,sep.c_str());
    while(current != NULL){
        arr.push_back(current);
        current=strtok(NULL, sep.c_str());
    }
    return arr;
}



int solveEquation(string equation){
    vector<string> statement = split(equation, " ");
    string operand = statement[1];
    int val1 = stoi(statement[2]);
    int val2 = stoi(statement[4]);
    
    /*
     * Equations will have the form [var (operand) val1 = val2]
     */
    
    int returnVal = 0; //Solution to equation
    
    if (operand == "+"){
        returnVal = val2 - val1;
    } else if (operand == "-"){
        returnVal = val2 + val1;
    } else if (operand == "/"){
        returnVal = val2 * val1;
    } else {
        returnVal = val2 / val1;
    }
    return returnVal;
}

map<int,string> makeMap(char variable, int value){
    map<int, string> codeMap;
    int variableAscii = variable;
    int start = 'a';
    int firstValue = value - (variableAscii - start);
    
    codeMap.insert(pair<int, string>(firstValue-1, " "));
    
    for(int i=0; i<26; i++){
        char letter = static_cast<char>(start + i);
        string sletter(1, letter);
        codeMap.insert(pair<int, string>(firstValue + i, sletter));
    }
    return codeMap;
}

string decodeMessage(map<int, string> key, string message){
    vector<string> messageSplit = split(message, " ");
    string decodedMessage;
    for (auto & element : messageSplit){
        int val = stoi(element);
        decodedMessage += key[val];
    }
    return decodedMessage;
}


int main(int argc, const char * argv[]) {
    bool reading = true;
    vector<string> equations;
    vector<string> messages;
    string line;
    
    while(reading) {
        getline(cin, line);
        if (line == "end"){
            reading = false;
        } else {
            equations.push_back(line);
            getline(cin, line);
            messages.push_back(line);
        }
    }
    
    int caseNum = 0;
    
    for(auto & element : equations){
        int val = solveEquation(element);
        vector<string> statement = split(element, " ");
        map<int,string> keyring = makeMap(statement[0][0], val);
        cout << "Message " << (caseNum + 1) << ": " << decodeMessage(keyring, messages[caseNum]) << endl;
        caseNum++;
    }
    
    
    return 0;
}
    

