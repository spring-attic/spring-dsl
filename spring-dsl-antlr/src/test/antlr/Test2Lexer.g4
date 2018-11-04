// taking partial grammar from a spring statemachine to test various concepts
lexer grammar Test2Lexer;

STATE                :   [Ss][Tt][Aa][Tt][Ee] ;
STATEMACHINE         :   [Ss][Tt][Aa][Tt][Ee][Mm][Aa][Cc][Hh][Ii][Nn][Ee] ;
LBRACE               :   '{' ;
RBRACE               :   '}' ;
TRANSITION           :   [Tt][Rr][Aa][Nn][Ss][Ii][Tt][Ii][Oo][Nn] ;
INITIAL              :   [Ii][Nn][Ii][Tt][Ii][Aa][Ll] ;
END                  :   [Ee][Nn][Dd] ;
SOURCE               :   [Ss][Oo][Uu][Rr][Cc][Ee] ;
TARGET               :   [Tt][Aa][Rr][Gg][Ee][Tt] ;
ID                   :   LETTER (LETTER|DIGIT)* ;
fragment LETTER      :   [a-zA-Z\u0080-\u00FF_] ;
SEMI                 :   ';' ;
fragment DIGIT       :   [0-9] ;
WS                   :   [ \t\n\r]+ -> channel(HIDDEN) ;
