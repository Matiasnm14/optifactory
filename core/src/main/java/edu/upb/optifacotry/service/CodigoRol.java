package edu.upb.optifacotry.service;

import java.util.Arrays;

public enum CodigoRol {
    ADMIN,
    AUDITORIA,
    OPERACION,
    SUPERVISION_PRODUCCION,
    PERSONAL,
    CALIDAD,
    INVENTARIO,
    MANTENIMIENTO,
    RESPONSABLE_MANTENIMIENTO,
    SEGURIDAD_INDUSTRIAL,
    AMBIENTE;

    public static boolean definido(String codigo) {
        return Arrays.stream(values()).anyMatch(rol -> rol.name().equals(codigo));
    }
}
