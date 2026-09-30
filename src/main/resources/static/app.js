// petit script global chargé sur toutes les pages

document.addEventListener('DOMContentLoaded', function() {

    // on accroche une confirmation sur tous les liens de suppression
       // comme ça si le prof clique par erreur il peut annuler
    const deleteLinks = document.querySelectorAll('a[href*="supprimer"]');

    deleteLinks.forEach(function(link) {
        link.addEventListener('click', function(e) {
            if (!confirm('Êtes-vous sûr de vouloir supprimer cet élément ?')) {
                e.preventDefault(); // Annule la navigation
            }
        });
    });

    console.log(' IHEC App chargée');
});

// affiche une notification temporaire en haut à droite
// utilisé pour les retours utilisateur sans rechargement de page
function showMessage(message, type = 'success') {
    const div = document.createElement('div');
    div.className = 'alert alert-' + type;
    div.textContent = message;
    div.style.position = 'fixed';
    div.style.top = '20px';
    div.style.right = '20px';
    div.style.zIndex = '9999';

    document.body.appendChild(div);
 // disparaît automatiquement après 3 secondes
    setTimeout(function() {
        div.remove();
    }, 3000);
}