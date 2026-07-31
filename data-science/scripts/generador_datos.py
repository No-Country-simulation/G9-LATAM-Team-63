import os

import numpy as np
import pandas as pd


def generar_dataset_inicial(n=1500, seed=42, output_path='../data/processed/dataset_inicial.csv'):

    np.random.seed(seed)
    
    # Variables de entrada
    consumo_kwh         = np.random.randint(80, 950, n)
    cantidad_equipos    = np.random.randint(2, 22, n)
    horas_alto_consumo  = np.random.randint(2, 14, n)
    tipo_inmueble       = np.random.choice(
        ['Casa', 'Apartamento', 'Local', 'Oficina'],
        n,
        p=[0.5, 0.3, 0.1, 0.1]
    )
    uso_horario_pico    = np.random.choice([True, False], n, p=[0.45, 0.55])
    
    # Calcular ratio
    ratio = consumo_kwh / cantidad_equipos
    
    # Clasificación
    def clasificar(row):
        if row['ratio'] > 55 and row['horas_alto_consumo'] > 7:
            return 'Ineficiente'
        elif row['ratio'] < 28 and row['horas_alto_consumo'] < 4:
            return 'Eficiente'
        else:
            return 'Moderado'

    # DataFrame temporal para aplicar clasificación
    df_temp = pd.DataFrame({
        'consumo_kwh'       : consumo_kwh,
        'cantidad_equipos'  : cantidad_equipos,
        'horas_alto_consumo': horas_alto_consumo,
        'tipo_inmueble'     : tipo_inmueble,
        'uso_horario_pico'  : uso_horario_pico,
        'ratio'             : ratio
    })
    df_temp['categoria'] = df_temp.apply(clasificar, axis=1)

    # Dataset final
    df = df_temp.drop(columns=['ratio'])

    # Guardar
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    df.to_csv(output_path, index=False)
    
    return df


def generar_dataset_extendido(n=2000, seed=42, output_path='../data/processed/dataset_extendido.csv'):

    np.random.seed(seed)
    
    # Variables de entrada
    consumo_kwh         = np.random.randint(80, 1200, n)
    cantidad_equipos    = np.random.randint(2, 25, n)
    horas_alto_consumo  = np.random.randint(2, 16, n)
    tipo_inmueble       = np.random.choice(
        ['Casa', 'Apartamento', 'Local', 'Oficina'],
        n,
        p=[0.4, 0.3, 0.15, 0.15]
    )
    uso_horario_pico    = np.random.choice([True, False], n, p=[0.45, 0.55])
    numero_habitantes   = np.random.randint(1, 7, n)
    antiguedad_inmueble = np.random.randint(0, 51, n)
    calefaccion         = np.random.choice([True, False], n, p=[0.35, 0.65])
    aire_acondicionado  = np.random.choice([True, False], n, p=[0.40, 0.60])
    
    # Si tiene calefacción eléctrica o aire acondicionado, incrementamos el consumo
    factor_consumo = np.ones(n)
    factor_consumo[calefaccion] *= 1.2              # +20% si tiene calefacción
    factor_consumo[aire_acondicionado] *= 1.3       # +30% si tiene aire acondicionado
    consumo_kwh = (consumo_kwh * factor_consumo).astype(int)
    consumo_kwh = np.clip(consumo_kwh, 80, 1500) 
    
    # Calcular ratio
    ratio = consumo_kwh / cantidad_equipos
    
    def clasificar(row):
        # Variables base
        ratio = row['ratio']
        horas = row['horas_alto_consumo']
        
        # Factor de ajuste (integra TODAS las variables)
        ajuste = 0
        
        # Por tipo de inmueble
        if row['tipo_inmueble'] in ['Local', 'Oficina']:
            ajuste += 8
        elif row['tipo_inmueble'] == 'Casa':
            ajuste += 3
        
        # Por uso horario pico
        if row['uso_horario_pico']:
            ajuste += 5
        
        # Por número de habitantes
        if row['numero_habitantes'] >= 4:
            ajuste += 4
        elif row['numero_habitantes'] >= 2:
            ajuste += 2
        
        # Por antigüedad
        if row['antiguedad_inmueble'] > 30:
            ajuste += 6
        elif row['antiguedad_inmueble'] > 15:
            ajuste += 3
        
        # Penalizaciones por calefacción y aire
        if row['calefaccion']:
            ajuste += 5
        if row['aire_acondicionado']:
            ajuste += 8
        
        # Clasificación final
        if (ratio > (55 + ajuste)) and (horas > 7):
            return 'Ineficiente'
        elif (ratio < (28 + ajuste)) and (horas < 4):
            return 'Eficiente'
        else:
            return 'Moderado'
    
    # Crear DataFrame temporal con todas las variables
    df_temp = pd.DataFrame({
        'consumo_kwh'           : consumo_kwh,
        'cantidad_equipos'      : cantidad_equipos,
        'horas_alto_consumo'    : horas_alto_consumo,
        'tipo_inmueble'         : tipo_inmueble,
        'uso_horario_pico'      : uso_horario_pico,
        'numero_habitantes'     : numero_habitantes,
        'antiguedad_inmueble'   : antiguedad_inmueble,
        'calefaccion'           : calefaccion,
        'aire_acondicionado'    : aire_acondicionado,
        'ratio'                 : ratio
    })
    
    # Aplicar clasificación
    df_temp['categoria'] = df_temp.apply(clasificar, axis=1)
    
    # Eliminar columna auxiliar 'ratio'
    df = df_temp.drop(columns=['ratio'])
    
    # Guardar CSV
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    df.to_csv(output_path, index=False)
    
    return df