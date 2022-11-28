grammar Calculator;


expression: multiplyingExpression ((PLUS | MINUS) multiplyingExpression)*;
multiplyingExpression: powExpression ((TIMES | DIV) powExpression)*;

powExpression
   : floatExpresion ((POW|SQRT) floatExpresion)*
   ;

floatExpresion
    :FLOAT|MINUS FLOAT
    ;

FLOAT
   : ('0' .. '9') + ('.' ('0' .. '9') +)?
   ;
SQRT: 'sqrt';
DOT: '.';
TIMES: '*' ;
DIV: '/' ;
PLUS: '+' ;
MINUS: '-' ;
POW:'^';
INTEGRAL: 'cal';
WS : [ \t\r\n]+ -> skip ;