### Movie Streaming App

## Overview

Movie Streaming App es una aplicación de Android diseñada para proporcionar información detallada sobre películas, incluyendo el título, la calificación de IMDb, el año de lanzamiento, la sinopsis, la duración, los actores, el país de origen y más. La aplicación también permite a los usuarios iniciar sesión, buscar películas por ID y marcar sus películas favoritas.

## Características

- **Inicio de Sesión:** Permite a los usuarios iniciar sesión con su correo electrónico y contraseña.
- **Detalles de Películas:** Muestra información detallada de las películas, incluyendo póster, título, calificación de IMDb, año de lanzamiento, sinopsis, duración, actores, país y géneros.
- **Lista de Actores y Géneros:** Muestra una lista de actores y géneros asociados a la película.
- **Favoritos:** Permite a los usuarios agregar o quitar películas de sus favoritos.

## Tecnologías Utilizadas

- **Lenguaje de Programación:** Java
- **IDE:** Android Studio
- **Librerías:** 
  - [Volley](https://developer.android.com/training/volley) para las solicitudes de red.
  - [OkHttp](https://square.github.io/okhttp/) para las solicitudes de red con cuerpo JSON.
  - [Glide](https://bumptech.github.io/glide/) para la carga de imágenes.
  - [Gson](https://github.com/google/gson) para la deserialización de JSON.
- **API:** `https://api.cosomovies.xyz/api`
  

## Instalación y Configuración

1. **Clona el Repositorio:**
   ```sh
   git clone https://github.com/tu_usuario/movie-details-app.git
   cd movie-details-app
   ```

2. **Abre el Proyecto en Android Studio:**
   - Abre Android Studio.
   - Selecciona "Open an existing Android Studio project".
   - Navega al directorio donde clonaste el repositorio y selecciónalo.

3. **Configura las Dependencias:**
   - Android Studio debería sincronizar automáticamente las dependencias del proyecto. Si no, haz clic en "Sync Now" en la barra de notificaciones que aparece en la parte superior de la ventana.

4. **Genera el APK:**
   - Ve a `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
   - Una vez completado, encontrarás el APK en el directorio `app/build/outputs/apk/debug`.

## Uso de la Aplicación

### Inicio de Sesión

1. **Abre la Aplicación:**
   - Instala el APK generado en tu dispositivo Android.
   - Abre la aplicación.

2. **Inicia Sesión:**
   - Introduce tu correo electrónico y contraseña en la pantalla de inicio de sesión.
   - Haz clic en el botón "Login".

### Visualización de Detalles de Películas

1. **Buscar Película:**
   - Después de iniciar sesión, busca una película por su ID.
   - La aplicación enviará una solicitud a `https://api.cosomovies.xyz/api/utils/search/id/<ID de la pelicula>` con el usuario en el cuerpo de la solicitud.

2. **Ver Detalles:**
   - La aplicación mostrará los detalles de la película, incluyendo póster, título, calificación de IMDb, año de lanzamiento, sinopsis, duración, actores, país y géneros.

### Favoritos

1. **Agregar o Quitar Favoritos:**
   - En la pantalla de detalles de la película, haz clic en el icono de favoritos para agregar o quitar la película de tus favoritos.
   - El icono cambiará de color para indicar si la película está en tus favoritos.

## Contribuciones

Proyecto trabajado por:
- Alberth Godoy
- Kevin Banegas
- Emiliano Agurcia
- Walther Carrasco
- Wilmer Zuniga



---

**¡Gracias por usar Movie Streaming App!** Si tienes alguna pregunta o sugerencia, no dudes en abrir un issue en el repositorio.
