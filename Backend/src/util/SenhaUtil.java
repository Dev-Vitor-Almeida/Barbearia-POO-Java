package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class SenhaUtil {
    public static String gerarHash(String senha) {
        if (senha == null) {
            return "";
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder resultado = new StringBuilder();

            for (byte item : hash) {
                resultado.append(String.format("%02x", item));
            }

            return resultado.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash da senha.", e);
        }
    }

    public static boolean conferir(String senhaDigitada, String senhaSalva) {
        if (senhaDigitada == null || senhaSalva == null) {
            return false;
        }

        return senhaSalva.equals(gerarHash(senhaDigitada)) || senhaSalva.equals(senhaDigitada);
    }
}
