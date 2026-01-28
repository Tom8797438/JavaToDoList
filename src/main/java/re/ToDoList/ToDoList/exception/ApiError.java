//format standard des erreurs
package re.ToDoList.ToDoList.exception;

import java.time.Instant; // pour un timestamp précis (UTC)
import java.util.Map; // stocker des détails flexibles

public record ApiError( // record = classe permet de produire des objets avec un état initial mais qui ne pourront plus changer d'état une fois instanciés.
//Génère automatiquement : constructeur, getters (timestamp(), status()), equals/hashCode, toString.
        Instant timestamp,
        int status, // optionnel :code HTTP numérique
        String error, // optionnel : nom de l'erreur HTTP
        String message,
        String path,
        Map<String, Object> details
) {}
