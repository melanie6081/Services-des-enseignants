package champollion;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Enseignant extends Personne {

    private HashMap<UE, ServicePrevu> heuresParUE = new HashMap<>();
    private ArrayList<Intervention> interventions = new ArrayList<>();

    public Enseignant(String nom, String email) {
        super(nom, email);
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant en "heures équivalent TD" Pour le calcul : 1 heure
     * de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure de TP vaut 0,75h
     * "équivalent TD"
     *
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     *
     */
    public int heuresPrevues() {
        int heuresTotales = 0;
        for ( Map.Entry<UE, ServicePrevu> u : heuresParUE.entrySet()){
            heuresTotales = heuresTotales + heuresPrevuesPourUE(u.getKey());
        }
        return heuresTotales;
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant dans l'UE spécifiée en "heures équivalent TD" Pour
     * le calcul : 1 heure de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure
     * de TP vaut 0,75h "équivalent TD"
     *
     * @param ue l'UE concernée
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     *
     */
    public int heuresPrevuesPourUE(UE ue) {
        int heures;
        double heuresCM = heuresParUE.get(ue).getVolumeCM()*1.5;
        double heuresTD = heuresParUE.get(ue).getVolumeTD();
        double heuresTP = heuresParUE.get(ue).getVolumeCM()*0.75;
        heures = (int) (heuresCM + heuresTD + heuresTP);
        return heures;
    }

    /**
     * Ajoute un enseignement au service prévu pour cet enseignant
     *
     * @param ue l'UE concernée
     * @param volumeCM le volume d'heures de cours magitral
     * @param volumeTD le volume d'heures de TD
     * @param volumeTP le volume d'heures de TP
     */
    public void ajouteEnseignement(UE ue, int volumeCM, int volumeTD, int volumeTP) {
        int newVolumeCM;
        int newVolumeTD;
        int newVolumeTP;
        if (heuresParUE.containsKey(ue)){
                heuresParUE.replace(ue, new ServicePrevu(volumeCM + heuresParUE.get(ue).getVolumeCM(), volumeTD + heuresParUE.get(ue).getVolumeTD(), volumeTP+ heuresParUE.get(ue).getVolumeTP()));

            } else {
                heuresParUE.put(ue, new ServicePrevu(volumeCM,volumeTD,volumeTP));
        }

    }

    public void ajouteIntervention(Intervention inter){
        interventions.add(inter);
    }

    public boolean enSousService(){
        boolean sousService = false;
        if (heuresPrevues()<192){
            sousService = true;
        }
        return sousService;
    }

    public int resteAPlanifier(UE ue, TypeIntervention type){
        int reste = heuresPrevues();

        for (Intervention i : interventions){
            if(!i.isAnnulee()){
                switch (i.getType()){
                    case CM:
                        reste = (int) (reste - (i.getDuree()*1.5));
                        break;
                    case TD:
                        reste = (int) (reste - i.getDuree());
                        break;
                    case TP:
                        reste = (int) (reste - (i.getDuree()*0.75);
                        break;
                }
                
            }
        }
        return reste;
    }

    public ArrayList<Intervention> getInterventions() {
        return interventions;
    }
}
