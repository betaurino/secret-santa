
# Secret Santa

Este proyecto te permite exponer una REST API con la funcionalidad de generar un secret santa con tu familia y amigos, es muy simple, y evita que te toquen miembros de tu familia inmediata o que te toque repetida la misma persona de hasta 3 años atras. 




## API Reference
Los siguientes son los metodos que expone la REST API para poder crear una familia, actualizar los datos de la misma, consultarla, y poder generar las asignaciones del secret santa.

#### Get family members

```http
  GET /secretSanta/family/${id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `string` | **Required**. Family id |

#### Create family list

```http
  POST /secretSanta/family
```
###### Request body example
```JSON
{
    "familyId" : {
        "id" : "Cruz"
    },
  "familyMembers": [
    {
        "name":"Bob",
        "immediateFamily":[]
    },
    {
        "name":"Juan",
        "immediateFamily":["Sofy"]
    },
    {
        "name":"Sofy",
        "immediateFamily":["Juan"]
    },
    {
        "name":"Greta",
        "immediateFamily":[]
    }
  ]
}
```

#### Update family list

```http
  PUT /secretSanta/family/${id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `id` | `string` | **Required**. Family id |

###### Request body example
```JSON
[
    {
        "name":"Bob",
        "immediateFamily":[]
    },
    {
        "name":"Juan",
        "immediateFamily":["Sofy"]
    },
    {
        "name":"Sofy",
        "immediateFamily":["Juan", "Greta"]
    },
    {
        "name":"Greta",
        "immediateFamily":["Sofy"]
    }
]
```

#### Assign secret santa

```http
  POST /secretSanta/family/${id}
```
###### Request body example
```JSON
{
    "id" : "Cruz"
}
```


## Deployment

Para desplegar este proyecto asegurate de tener instalada la version más reciente del JDK de java y tener instalado gradle, despues ejecuta:

```bash
  git clone https://github.com/betaurino/secret-santa.git
```

```bash
  ./gradlew bootRun
```


