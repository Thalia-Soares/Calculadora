package com.example.Thalia.unisal.minhacalculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Thalia.unisal.minhacalculadora.ui.theme.MinhaCalculadoraTheme

class MainActivity : ComponentActivity() {

    // Valor que aparece no visor
    var visor by mutableStateOf("0")

    // Pilhas usadas para guardar operadores e operandos
    val pilhaOperador = mutableListOf<String>()
    val pilhaOperando = mutableListOf<String>()

    // Guarda somente o número que está sendo digitado
    var numeroAtual = "0"

    // Indica que o próximo número será um novo operando
    var aguardandoOperando = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MinhaCalculadoraTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    criaCalculadora(visor)
                }
            }
        }
    }
    // Monta a interface da calculadora
    @Composable
    fun criaCalculadora(visor: String) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 32.sp,
                text = visor
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                criaBotaoPequeno("Sin", BotaoOperacao.SENO)
                criaBotaoPequeno("Cos", BotaoOperacao.COSSENO)
                criaBotaoPequeno("Tan", BotaoOperacao.TANGENTE)
                criaBotaoPequeno("√", BotaoOperacao.RAIZ)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                criaBotaoPequeno("!", BotaoOperacao.FATORIAL)
                criaBotaoPequeno("π", BotaoOperacao.PI)
                criaBotaoPequeno("Inv", BotaoOperacao.INVERSO)
                criaBotaoPequeno("^", BotaoOperacao.POTENCIA)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                criaBotaoPequeno("%", BotaoOperacao.PERCENTUAL)
                criaBotaoPequeno("/", BotaoOperacao.DIVISAO)
                criaBotaoPequeno("*", BotaoOperacao.MULTIPLICACAO)
                criaBotaoPequeno("-", BotaoOperacao.SUBTRACAO)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                criaBotaoPequeno("7", BotaoOperacao.SETE)
                criaBotaoPequeno("8", BotaoOperacao.OITO)
                criaBotaoPequeno("9", BotaoOperacao.NOVE)
                criaBotaoPequeno("+", BotaoOperacao.SOMA)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                criaBotaoPequeno("4", BotaoOperacao.QUATRO)
                criaBotaoPequeno("5", BotaoOperacao.CINCO)
                criaBotaoPequeno("6", BotaoOperacao.SEIS)
                criaBotaoPequeno(",", BotaoOperacao.VIRGULA)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                criaBotaoPequeno("1", BotaoOperacao.UM)
                criaBotaoPequeno("2", BotaoOperacao.DOIS)
                criaBotaoPequeno("3", BotaoOperacao.TRES)
                criaBotaoPequeno("=", BotaoOperacao.IGUALDADE)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                criaBotaoPequeno("+/-", BotaoOperacao.MAIS_MENOS)
                criaBotaoPequeno("0", BotaoOperacao.ZERO)
                criaBotaoPequeno("C", BotaoOperacao.LIMPAR)
                criaBotaoPequeno("<-", BotaoOperacao.APAGAR)
            }
        }
    }

    // Cria os botões da calculadora
    @Composable
    fun criaBotaoPequeno(
        texto: String,
        identificador: BotaoOperacao
    ) {

        Button(
            modifier = Modifier
                .width(80.dp)
                .height(50.dp),

            onClick = {

                // Os primeiros itens da enumeração são os números
                if (identificador.ordinal <= BotaoOperacao.VIRGULA.ordinal) {
                    numPress(identificador)
                } else {
                    opPress(identificador)
                }
            },

            colors = if (identificador == BotaoOperacao.IGUALDADE) {
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            } else {
                ButtonDefaults.buttonColors()
            }
        ) {
            Text(texto)
        }
    }


    fun numPress(identificador: BotaoOperacao) {

        // Se estava esperando outro operando, começa um novo número
        if (aguardandoOperando) {
            numeroAtual = "0"
            aguardandoOperando = false
        }

        // Não permite vários zeros no começo do número
        if (numeroAtual == "0" && identificador == BotaoOperacao.ZERO) {
            return
        }

        // Não permite duas vírgulas
        if (numeroAtual.contains(",") &&
            identificador == BotaoOperacao.VIRGULA
        ) {
            return
        }

        // Descobre qual número foi pressionado
        var tmp = identificador.name

        if (tmp == BotaoOperacao.VIRGULA.name) {
            tmp = ","
        } else {
            tmp = identificador.ordinal.toString()
        }

        // Substitui o zero inicial ou acrescenta o novo número
        if (numeroAtual == "" || numeroAtual == "0") {
            numeroAtual = tmp
        } else {
            numeroAtual += tmp
        }

        // Atualiza o visor
        if (visor == "0") {
            visor = numeroAtual
        } else if (
            visor.endsWith(" + ") ||
            visor.endsWith(" - ") ||
            visor.endsWith(" * ") ||
            visor.endsWith(" / ") ||
            visor.endsWith(" % ") ||
            visor.endsWith(" ^ ")
        ) {
            // Primeiro número depois do operador
            visor += numeroAtual
        } else {
            // Continua acrescentando no número atual
            val partes = visor.split(" ").toMutableList()

            if (partes.size >= 3) {
                partes[partes.lastIndex] = numeroAtual
                visor = partes.joinToString(" ")
            } else {
                visor = numeroAtual
            }
        }
    }


    fun opPress(identificador: BotaoOperacao) {

        // Limpar calculadora
        if (identificador == BotaoOperacao.LIMPAR) {
            pilhaOperador.clear()
            pilhaOperando.clear()
            numeroAtual = "0"
            visor = "0"
            aguardandoOperando = false
            return
        }

        // Igualdade
        if (identificador == BotaoOperacao.IGUALDADE) {
            igualdade()
            return
        }

        // Apagar um item por vez
        if (identificador == BotaoOperacao.APAGAR) {

            val partes = visor.split(" ").toMutableList()

            // Apaga os dígitos do número atual
            if (numeroAtual.isNotEmpty()) {

                numeroAtual = numeroAtual.dropLast(1)

                if (numeroAtual.isNotEmpty()) {
                    partes[partes.lastIndex] = numeroAtual
                    visor = partes.joinToString(" ")
                } else {
                    partes.removeAt(partes.lastIndex)
                    visor = partes.joinToString(" ")
                }

            } else if (partes.size >= 2) {

                // Quando chega no operador, apaga o operador
                partes.removeAt(partes.lastIndex)
                visor = partes.joinToString(" ")

                // Recupera o número anterior
                if (partes.isNotEmpty()) {
                    numeroAtual = partes.last()
                }

                aguardandoOperando = false

                // Mantém as pilhas sincronizadas
                if (pilhaOperador.isNotEmpty()) {
                    pilhaOperador.removeAt(pilhaOperador.lastIndex)
                }

                if (pilhaOperando.isNotEmpty()) {
                    pilhaOperando.removeAt(pilhaOperando.lastIndex)
                }
            }

            if (visor.isEmpty()) {
                visor = "0"
                numeroAtual = "0"
            }

            return
        }

        // Troca o sinal do número
        if (identificador == BotaoOperacao.MAIS_MENOS) {

            val numero = numeroAtual.toFloat() * -1
            numeroAtual = numero.toString().replace(".", ",")
            visor = numeroAtual

            return
        }

        // Insere o valor de Pi
        if (identificador == BotaoOperacao.PI) {

            numeroAtual = "3,14"

            if (aguardandoOperando) {
                visor += numeroAtual
                aguardandoOperando = false
            } else {
                visor = numeroAtual
            }

            return
        }

        // Operações que usam apenas um número
        if (
            identificador == BotaoOperacao.SENO ||
            identificador == BotaoOperacao.COSSENO ||
            identificador == BotaoOperacao.TANGENTE ||
            identificador == BotaoOperacao.RAIZ ||
            identificador == BotaoOperacao.FATORIAL ||
            identificador == BotaoOperacao.INVERSO
        ) {
            operacaoUnaria(identificador)
            return
        }

        // Se apertar outro operador, troca o anterior
        if (aguardandoOperando) {

            if (pilhaOperador.isNotEmpty()) {
                pilhaOperador[pilhaOperador.lastIndex] = identificador.name
            }

            if (visor.contains(" ")) {
                val partes = visor.split(" ")

                if (partes.size >= 2) {
                    visor = partes.dropLast(2).joinToString(" ") +
                            " " + simboloOperador(identificador) + " "
                }
            }

            return
        }

        // Guarda o número e o operador nas pilhas
        pilhaOperando.add(numeroAtual)
        pilhaOperador.add(identificador.name)

        // Mostra o operador no visor
        visor += " " + simboloOperador(identificador) + " "

        aguardandoOperando = true
    }


    fun igualdade() {

        if (pilhaOperador.isEmpty() || pilhaOperando.isEmpty()) {
            return
        }

        // Guarda o último número digitado
        pilhaOperando.add(numeroAtual)

        // Começa pelo primeiro número da pilha
        var resultado = pilhaOperando[0]
            .replace(",", ".")
            .toFloat()

        // Percorre os operadores na ordem em que foram digitados
        for (i in pilhaOperador.indices) {

            val operador = pilhaOperador[i]
            val aux = pilhaOperando[i + 1]
                .replace(",", ".")
                .toFloat()

            // Realiza a operação
            if (operador == BotaoOperacao.SOMA.name) {
                resultado += aux

            } else if (operador == BotaoOperacao.SUBTRACAO.name) {
                resultado -= aux

            } else if (operador == BotaoOperacao.MULTIPLICACAO.name) {
                resultado *= aux

            } else if (operador == BotaoOperacao.DIVISAO.name) {

                if (aux == 0f) {
                    visor = "Erro"
                    pilhaOperador.clear()
                    pilhaOperando.clear()
                    aguardandoOperando = true
                    return
                }

                resultado /= aux

            } else if (operador == BotaoOperacao.PERCENTUAL.name) {
                resultado = resultado * aux / 100

            } else if (operador == BotaoOperacao.POTENCIA.name) {
                resultado = Math.pow(
                    resultado.toDouble(),
                    aux.toDouble()
                ).toFloat()
            }
        }

        // Mostra o resultado no visor
        numeroAtual = resultado.toString().replace(".", ",")

        // Remove o ,0 de números inteiros
        if (numeroAtual.endsWith(",0")) {
            numeroAtual = numeroAtual.dropLast(2)
        }

        visor = numeroAtual

        // Limpa as pilhas para uma nova operação
        pilhaOperador.clear()
        pilhaOperando.clear()

        aguardandoOperando = true
    }

    // Realiza operações que usam apenas um número
    fun operacaoUnaria(identificador: BotaoOperacao) {

        try {

            val valor = numeroAtual
                .replace(",", ".")
                .toDouble()

            var resultado = 0.0

            if (identificador == BotaoOperacao.SENO) {

                // Seno usando graus
                resultado = Math.sin(Math.toRadians(valor))

            } else if (identificador == BotaoOperacao.COSSENO) {

                // Cosseno usando graus
                resultado = Math.cos(Math.toRadians(valor))

            } else if (identificador == BotaoOperacao.TANGENTE) {

                // Tangente usando graus
                resultado = Math.tan(Math.toRadians(valor))

            } else if (identificador == BotaoOperacao.RAIZ) {

                // Não permite raiz de número negativo
                if (valor < 0) {
                    visor = "Erro"
                    return
                }

                resultado = Math.sqrt(valor)

            } else if (identificador == BotaoOperacao.INVERSO) {

                // Não permite divisão por zero
                if (valor == 0.0) {
                    visor = "Erro"
                    return
                }

                resultado = 1 / valor

            } else if (identificador == BotaoOperacao.FATORIAL) {

                // Fatorial somente de números inteiros de 0 a 20
                if (valor < 0 || valor % 1 != 0.0 || valor > 20) {
                    visor = "Erro"
                    return
                }

                resultado = 1.0
                var i = 1

                while (i <= valor.toInt()) {
                    resultado *= i
                    i++
                }
            }

            numeroAtual = resultado.toString().replace(".", ",")

            // Remove o ,0 de números inteiros
            if (numeroAtual.endsWith(",0")) {
                numeroAtual = numeroAtual.dropLast(2)
            }

            visor = numeroAtual

        } catch (e: Exception) {

            // Evita que a calculadora feche por causa de um valor inválido
            visor = "Erro"
        }
    }


    // Retorna o símbolo que será mostrado no visor
    fun simboloOperador(identificador: BotaoOperacao): String {

        if (identificador == BotaoOperacao.SOMA) {
            return "+"
        } else if (identificador == BotaoOperacao.SUBTRACAO) {
            return "-"
        } else if (identificador == BotaoOperacao.MULTIPLICACAO) {
            return "*"
        } else if (identificador == BotaoOperacao.DIVISAO) {
            return "/"
        } else if (identificador == BotaoOperacao.PERCENTUAL) {
            return "%"
        } else if (identificador == BotaoOperacao.POTENCIA) {
            return "^"
        }

        return ""
    }


    enum class BotaoOperacao {

        // Números
        ZERO,
        UM,
        DOIS,
        TRES,
        QUATRO,
        CINCO,
        SEIS,
        SETE,
        OITO,
        NOVE,
        VIRGULA,

        // Operações
        SOMA,
        SUBTRACAO,
        MULTIPLICACAO,
        DIVISAO,
        PERCENTUAL,
        POTENCIA,
        IGUALDADE,
        LIMPAR,

        // Operações científicas
        SENO,
        COSSENO,
        TANGENTE,
        FATORIAL,
        PI,
        INVERSO,
        RAIZ,

        // Outras funções
        MAIS_MENOS,
        APAGAR
    }
}



