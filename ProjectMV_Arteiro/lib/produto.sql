--DROP TABLE PRODUTOS;
--DROP SEQUENCE SEQ_TESTE;
--DROP PROCEDURE REAJUSTE_POR_PRODUTO;
--DROP PROCEDURE REAJUSTE_TODOS_PRODUTOS;
--DROP PACKAGE PKG_MV;

CREATE SEQUENCE SEQ_TESTE NOCACHE;
CREATE TABLE PRODUTOS (ROW_ID INT, DESCRICAO VARCHAR(255), PRECO FLOAT(126));
INSERT INTO PRODUTOS (ROW_ID, DESCRICAO, PRECO) VALUES (SEQ_TESTE.NEXTVAL, 'NINTENDO SWITCH', '300,10');
INSERT INTO PRODUTOS (ROW_ID, DESCRICAO, PRECO) VALUES (SEQ_TESTE.NEXTVAL, 'SONY PS4', '300,20');

--SELECT * FROM PRODUTOS;

CREATE OR REPLACE PACKAGE PKG_MV AS
    PROCEDURE REAJUSTE_POR_PRODUTO (P_REAJUSTE FLOAT, IDENTIFICADOR INT);
    PROCEDURE REAJUSTE_TODOS_PRODUTOS(P_REAJUSTE FLOAT);
END PKG_MV; 
/ 

CREATE OR REPLACE PACKAGE BODY PKG_MV AS
    PROCEDURE REAJUSTE_POR_PRODUTO (P_REAJUSTE FLOAT, IDENTIFICADOR INT) AS
        ERR_CODE VARCHAR2 (30);
        ERR_MSG VARCHAR2 (3000);
        V_TAXA FLOAT(126);
        V_VALOR PRODUTOS.PRECO%TYPE;
        
        CURSOR C_PRODUTOS IS
            SELECT ROW_ID, DESCRICAO, PRECO FROM produtos WHERE ROW_ID = IDENTIFICADOR;
            
        BEGIN
            DBMS_OUTPUT.PUT_LINE ('Iniciando REAJUSTE POR PRODUTO...');
            V_TAXA := 1 + (P_REAJUSTE / 100);
            DBMS_OUTPUT.PUT_LINE ('Calculado Multiplicador de ' || P_REAJUSTE || '%: "' || V_TAXA || '".');
            FOR REG IN C_PRODUTOS
            LOOP
                BEGIN
                DBMS_OUTPUT.PUT_LINE ('-----------------------------------------------------------------------------------------------------------------');
                V_VALOR := REG.PRECO*V_TAXA;
                DBMS_OUTPUT.PUT_LINE ('Atualizando Produto: "' || reg.DESCRICAO || '.');
                DBMS_OUTPUT.PUT_LINE ('Preco Anterior: R$' || REG.PRECO || ', Novo preço: R$' || V_VALOR || '.');
                UPDATE PRODUTOS SET PRECO = V_VALOR WHERE ROW_ID = REG.ROW_ID;
                COMMIT;
                DBMS_OUTPUT.PUT_LINE ('Atualizado.');
                EXCEPTION
                    WHEN OTHERS
                    THEN
                        ERR_CODE := SQLCODE;
                        ERR_MSG := SQLERRM;
                        DBMS_OUTPUT.put_line (ERR_CODE || ' - ' || ERR_MSG);
                END;
            END LOOP;
            DBMS_OUTPUT.PUT_LINE ('-----------------------------------------------------------------------------------------------------------------');
            DBMS_OUTPUT.PUT_LINE ('Finalizado!');
        EXCEPTION
           WHEN OTHERS
           THEN
              DBMS_OUTPUT.PUT_LINE ('-----------------------------------------------------------------------------------------------------------------');
              ERR_CODE := SQLCODE;
              ERR_MSG := SQLERRM;
              DBMS_OUTPUT.put_line (err_code || ' - ' || err_msg);
              DBMS_OUTPUT.PUT_LINE ('Finalizado!');
        END;
    
    PROCEDURE REAJUSTE_TODOS_PRODUTOS(P_REAJUSTE FLOAT) AS
        ERR_CODE VARCHAR2 (30);
        ERR_MSG VARCHAR2 (3000);
        CURSOR C_PRODUTOS IS
            SELECT ROW_ID, DESCRICAO, PRECO FROM produtos;
            
        BEGIN
            DBMS_OUTPUT.PUT_LINE ('Iniciando AJUSTE PARA TODOS OS REGISTROS...');
            FOR REG IN C_PRODUTOS
            LOOP
                BEGIN
                    DBMS_OUTPUT.PUT_LINE ('Atualizando Produto: ' || REG.ROW_ID || '.');
                    REAJUSTE_POR_PRODUTO (P_REAJUSTE, REG.ROW_ID);
                END;
            END LOOP;
            DBMS_OUTPUT.PUT_LINE ('-----------------------------------------------------------------------------------------------------------------');
            DBMS_OUTPUT.PUT_LINE ('Finalizado para todos os Produtos!');
        EXCEPTION
           WHEN OTHERS
           THEN
              DBMS_OUTPUT.PUT_LINE ('-----------------------------------------------------------------------------------------------------------------');
              err_code := SQLCODE;
              err_msg := SQLERRM;
              DBMS_OUTPUT.put_line (err_code || ' - ' || err_msg);
        END;
END PKG_MV; 
/ 

--EXEC PKG_MV.REAJUSTE_TODOS_PRODUTOS('10,5');
--EXECUTE PKG_MV.REAJUSTE_POR_PRODUTO ('10,5', 14);