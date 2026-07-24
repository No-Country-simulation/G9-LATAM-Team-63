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