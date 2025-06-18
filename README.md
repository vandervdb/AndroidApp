# 🎧 android-compose-spotify

<p align="center">
  <strong>Application Spotify en Jetpack Compose avec architecture modulaire</strong><br />
  <em>Scalable, typée, prête pour la prod.</em>
</p>

<p align="center">
  <img alt="tech" src="https://img.shields.io/badge/tech-jetpack_compose-blue?style=flat-square" />
  <img alt="hilt" src="https://img.shields.io/badge/DI-hilt-blueviolet?style=flat-square" />
  <img alt="license" src="https://img.shields.io/badge/license-MIT-green?style=flat-square" />
</p>

---

## 🚀 Aperçu

Projet Android construit avec :

- 🖌️ **Jetpack Compose**
- 🗄️ **DataStore** pour le stockage local
- 🔌 **Spotify Remote SDK**
- 🧩 **Hilt** pour l'injection de dépendances
- 🔄 **Ktor** pour les appels réseau
- 🧱 Multi-modules (\`core_ui\`, \`spotifyclient\`, etc.)

---

## 📁 Structure du projet

```bash
AndroidApp/
├── app/                    # Application principale Compose
├── core_ui/                # Composants et interfaces UI
├── spotifyclient/          # Client Spotify (auth + remote)
├── spotifyclient-fake/     # Implémentations Fake des clients Spotify
├── spotify_fake/           # ViewModel MiniPlayer factice
```

---

## 🛠️ Installation & lancement

```bash
# Compiler l'application
./gradlew assembleDebug

# Lancer sur un émulateur connecté
./gradlew installDebug
```

---

## ⚙️ Configuration des secrets

Créez un fichier `local.properties` à la racine du projet contenant :

```properties
CLIENT_ID=your-spotify-client-id
CLIENT_SECRET=your-spotify-client-secret
```

Ces valeurs sont chargées durant la compilation pour générer les constantes `BuildConfig` utilisées par `spotifyclient`.

---

## 🧱 Ajout de module partagé

```bash
# Exemple : création d'un module "nouvelle-lib"
./gradlew :nouvelle-lib:assembleDebug
```

Déclarez le module dans `settings.gradle.kts` pour l'inclure au projet.

---

## 🧪 Tests (à venir)

> Des tests unitaires et instrumentés seront ajoutés pour chaque module.

---

<details>
<summary>📦 Modules & librairies utilisés</summary>

| Module/librairie       | Description                                             |
| ---------------------- | ------------------------------------------------------- |
| `app`                  | Application Android Jetpack Compose                     |
| `core_ui`              | UI commune et contrats du MiniPlayer                    |
| `spotifyclient`        | Connexion Spotify Remote & authentification             |
| `spotifyclient-fake`   | Fakes pour tester sans Spotify                          |
| `spotify_fake`         | ViewModel et données factices pour l'UI                 |

</details>

---

## ✨ À venir

- 🌟 Intégration complète du lecteur Spotify
- 🔄 Refresh token automatique
- 🧪 Tests unitaires + e2e
- 🎨 Amélioration de l'interface Material3

---

## 👨‍💻 Auteur

Développé par **Arnaud Vanderbecq**  
[GitHub](https://github.com/vandervdb) · [LinkedIn](https://linkedin.com/in/avanderbecq)

---

## 🪪 Licence

MIT
