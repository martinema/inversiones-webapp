package com.inversiones.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ImpermanentLossCalculator {

    // Clase interna para representar una transacción o aporte
    static class Transaction {
        double rntPrice; // Precio de RNT en ese momento
        double rntAmount; // Cantidad de RNT aportada (puede ser 0 si es solo USDT)
        double usdtAmount; // Cantidad de USDT aportada (puede ser 0 si es solo RNT)
        String date; // Fecha opcional para referencia

        public Transaction(double rntPrice, double rntAmount, double usdtAmount, String date) {
            this.rntPrice = rntPrice;
            this.rntAmount = rntAmount;
            this.usdtAmount = usdtAmount;
            this.date = date;
        }
    }

    public static void main(String[] args) {
        // Configuración para decimales
        Locale.setDefault(Locale.US);

        // 1. Definir el historial de aportaciones
        // Ejemplo:
        // Entrada 1: RNT a $0.20, aportas 1000 RNT (equivalente a $200) y $200 USDT
        // Entrada 2 (Reinversión): RNT a $0.25, aportas 50 RNT extra (recogidos de recompensas)
        List<Transaction> transactions = new ArrayList<>();

        // Aporte inicial (50/50)
        double initialRntPrice = 0.20;
        double initialInvestmentUSD = 1000; // $500 en RNT, $500 en USDT
        double initialRnt = initialInvestmentUSD / 2 / initialRntPrice;
        double initialUsdt = initialInvestmentUSD / 2;
        
        transactions.add(new Transaction(initialRntPrice, initialRnt, initialUsdt, "Inicio"));

        // Aporte posterior (Reinversión de recompensas)
        // Ejemplo: Mes 3, RNT a $0.25, aportas $100 de recompensas (50 RNT)
        // Asumimos que aportas en proporción 50/50 para mantener el balance, o simplemente RNT que el pool equilibra
        // Aquí simulamos que aportas RNT y el pool lo convierte, pero para simplificar el IL, 
        // calculamos lo que "habrías tenido" vs lo que "tienes".
        // Para este ejemplo, añadimos RNT extra y USDT extra para simular una inyección de capital.
        double rntPriceReinvest = 0.25;
        double rntAdded = 100; // 100 RNT extra de recompensas
        double usdtAdded = rntAdded * rntPriceReinvest; // Aportando el equivalente en USDT para mantener 50/50 ideal
        
        transactions.add(new Transaction(rntPriceReinvest, rntAdded, usdtAdded, "Reinversión 1"));

        // 2. Estado Actual del Pool
        double currentRntPrice = 0.296;
        double currentPoolValueUSD = 1069; // Valor real que ves en tu wallet

        // 3. Calcular el escenario "HODL" (Si no hubieras puesto en el pool)
        // Sumamos todo lo aportado y calculamos cuánto valdría hoy si solo guardáramos.
        double totalRntHeld = 0;
        double totalUsdtHeld = 0;

        for (Transaction t : transactions) {
            totalRntHeld += t.rntAmount;
            totalUsdtHeld += t.usdtAmount;
        }

        double hodlValue = (totalRntHeld * currentRntPrice) + totalUsdtHeld;

        // 4. Cálculo del Impermanent Loss
        // IL = (Valor Pool - Valor HODL) / Valor HODL
        // Nota: El valor del pool ya incluye el efecto de las recompensas ganadas y reinvertidas
        // si el valor actual ($1069) es el resultado neto.
        // Si $1069 es solo el capital + recompensas reinvertidas, la comparación es válida.
        
        double ilValue = currentPoolValueUSD - hodlValue;
        double ilPercentage = (ilValue / hodlValue) * 100;

        // Salida de resultados
        System.out.println("--- Informe de Impermanent Loss (Múltiples Aportes) ---");
        System.out.println("Aportes Totales Registrados: " + transactions.size());
        System.out.printf("Total RNT acumulado (teórico en billetera): %.4f RNT%n", totalRntHeld);
        System.out.printf("Total USDT acumulado (teórico en billetera): %.4f USDT%n", totalUsdtHeld);
        System.out.println("-------------------------------------------------------");
        System.out.printf("Precio RNT Actual: $%.4f%n", currentRntPrice);
        System.out.printf("Valor Actual del Pool (con recompensas): $%.2f%n", currentPoolValueUSD);
        System.out.printf("Valor si hubieras hecho HODL: $%.2f%n", hodlValue);
        System.out.println("-------------------------------------------------------");
        
        if (ilValue < 0) {
            System.out.printf("PÉRDIDA POR IMPERMANENT LOSS: -$%.2f (%.2f%%)%n", Math.abs(ilValue), Math.abs(ilPercentage));
        } else {
            System.out.printf("GANANCIA RELATIVA AL HODL (Rendimiento > IL): +$%.2f (%.2f%%)%n", ilValue, ilPercentage);
        }
        
        System.out.println("-------------------------------------------------------");
        System.out.println("Nota: Este cálculo asume que el 'Valor Actual del Pool'");
        System.out.println("incluye todas las recompensas reinvertidas hasta la fecha.");
    }
}
