
# Android Accesibility

Existen tres grandes grupos de accesibilidad y dentro de cada una de estas sus funcionalidades

- Funciones de soporte de audio
	* Live Transcribe
	* Live Caption
	* Sound Amplifier
	* Hearing aid support
- Funciones de soporte de visión
	* TalkBack
	* Lookout
	* Magnification
	* Color correction
- Funciones de soporte de movilidad
	* Switch Access
	* Voice Access
	* Accesibility Menu

## Requerimientos

- 	Google Play Services
-  Suite Accesibilidad Android

## Documentación

Los servicios de accesibilidad de Android use una tecnología llamada arbol de accesibilidad para entender la interfaz de usuario de la aplicación. Esto es construido por uno, pero debemos retocarlo para que tenga sentido para nuestros clientes.

A continuación se describe brevemente las considerciones que debemos tener.

### Accessibility API

#### Descripción de contenido

Para una tecnología de asistencia como TalkBack para representar un elemento a un usuario, la tecnología necesita una representación contextual para presentarlo. Para elementos sin valor de texto , o cuando el valor de texto no podría funcionar cuando lo presenta una tecnología de asistencia entonces se debe establecer un texto para `contentDescription`.

```xml
<Button
	...
	android:contentDescription="Submit"/>
```

```kotlin
submit.contentDescription = "Submit"
```

#### Importante para accesibilidad

Si un elemento es puramente decorativo como un ícono que acompaña un text, agregar este elemento al arbol de accesibilidad resultaría en toque innecesarios para el usuario TalkBack. Para este caso no debemos proveer este elemento  a estas tecnologías de asistencia. Se puede hacer esto estableciendo **importantForAccessibility** en **no**. Como otra opción, si un elemento que está usualmente oculto proporciona un contexto entonces podemos establecer el valor en **yes**.

```xml
android:importantForAccessibility="no"
```

```kotlin
textIcon.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
```

#### Hint

El `hint` también es leido por TalkBack como la etiqueta de texto. Esto es útil en los elementos `EditText` para proveer a los usuarios una sugerencia o aviso sobre la información  que deben ingresar.

TalkBack lee el `hint` despues del valor de texto de los elementos o **contentDescription** y tipo de elemento.

Un uso ideal sería dar una corta descripción de las consecuencias de presionar un botón por ejemplo. Un botón con un texto "Enviar" podría tener un hint "Enviar tu mensaje"

```xml
<EditText
	...
	android:hint="Correo electrónico" />
```
#### Título de accesibilidad

Al navegar por una pantalla con TalkBack, los usuarios que no tienen o tienen muy poca visión navegan deslizándose de izquierda a derecha sobre la pantalla. En una pantalla que tiene una gran cantidad de contenido, esto puedo resultar tedioso para navegar sobre contenido irrelevante para poder encontrar la información que se está buscando. Marcar sus encabezados como tal permite a los usuarios de TalkBack leer los encabezados omitiendo su contenido.

```xml
<TextView
	...
	android:accessibilityHeading="true" /> 
```

```
headingLabel.isAccessibilityHeading = true 
```
#### Tamaño mínimo
Todos los elementos, tales como buttons, checkboxes, switches y otros controles deben tener como mínimo 44 pixeles relativos para cumplir con las lineas guías de WCAG. Android recomienda usar como mínimo 48 pixeles relativos

```xml
<ImageButton
	...
	android:minHeight="48dp"
	android:minWidth="48dp" /> 
```

```kotlin
imageButton.minWidth = 48
imageButton.minHeight = 48 
```
#### Etiqueta para
Como se mencionó anteriormente con el **hint** podemos dar una sugerencia del tipo de dato que debe ingresar. Esto trabaja bien cuando el campo no está lleno. Una vez que se ha ingresado algun valor este `hint`desaperece y ya no es claro cual es el propósito del campo. Esto es una mala experiencia para persona que tengan una dificultad con la atención o con la memoria. La con la propiedad `labelFor` nos permite enlazar un elemento `TextView`a un `EditText` u otro control.

```xml
<LinearLayout
	...>
	
	<TextView
		...
		android:layout_marginBottom="0dp"
		android:labelFor="@+id/editText"
		android:text="@string/name" />

	<EditText
		...
		android:id="@+id/editText"
		android:layout_marginTop="0dp"
		android:text="" /> 

	
</LinearLayout>
```

```kotlin
textView.labelFor = editText.id 
```

#### Controles personalizados
- **Acciones de accesbilidad**
  
  EL `AccessibilityNodeInfo` es el lugar donde le indicamos a Android sobre las acciones de tecnología de asistencia a nuestra vista. Es una oportunidad que tenemos para dar a los clientes más información acerca de lo que nuestro control es capaz y como.
  
  Si se hace foco sobre un botón with TalkBack, este leerá "Tocar dos veces para activar" una vez el valor del elemento ha sido leido. Esto está compuesto de dos partes. El botón le dice a Android que es capaz de un evento **onClick**. Desde esta pieza de información, Android agrega "Tocar dos veces para". Luego "activar" es una cadena proveída por el botón.
  
	```kotlin
	class MyView: View {
		constructor(context: Context, attrs: AttributeSet): super(context, attrs)
		
		override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo?) {         
			super.onInitializeAccessibilityNodeInfo(info)         
			
			val click = AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.ACTION_CLICK, "open")         
			val longClick = AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.ACTION_LONG_CLICK, "show options")
			
			info?.addAction(click)
			info?.addAction(longClick)
		}
	} 
	```
  
- **Manejo de evento**
Cuando se agrega un método `setOnClickListener()` a una vista puede hacer la función correctamente when los clientes tocan la pantalla, eso no garantiza que la vista responderá a Voice Access, TalkBack u otro servicio de accesibilidad correctamente. Se debe validar si se necesita sobre escribir el `sendAccessibilityEvent()` o `sendAccessibilityEventUnchecked()`. Estos métodos son lanzados cada vez que se activa un evento de accesibilidad en la vista, incluido el enfoque y el toque.

	```kotlin
	class MyView: View {
	
		constructor(context: Context, attrs: AttributeSet): super(context, attrs)
		
		override fun sendAccessibilityEvent(eventType: Int) {
			super.sendAccessibilityEvent(eventType)
			
			when (eventType) {
				AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED -> background = Color.BLACK.toDrawable()
				
				AccessibilityEvent.TYPE_VIEW_FOCUSED -> background = Color.RED.toDrawable()
				}
			}
	}
	```

#### Cambio de texto
Si el valor de un texto cambio entonces el servicio de accesibilidad necesita saber de eso. Esto ayuda a Android que lo que se le repsenta a un usuario de accesibilidad es lo mismo que se está mostrando en la pantalla. Para realizar esto el control personalizado debe informar a Android que el valor ha cambiado mediante una publicación de un evento `TYPE_VIEW_TEXT_CHANGED`.

```kotlin
sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) 
```

### Información del nodo
Tiene 3 partes fundamentales

- **AccessibilityNodeInfo**

	Representa el contenido de la pantalla en el momento.

- **AccessibilityEvent**

	Para saber cuándo el sistema debe solicitar el contenido en pantalla existen los AccessibilityEvent. Los eventos se envian desde la aplicación al servicio de accesibilidad cuando ocurre un cambio en la interfaz de usuario

- **AccessibilityAction**

	La capacidad para que el servicio de accesibilidad actúe sobre la interfaz de usuario en nombre del usuario

## Pruebas

- Pruebas manuales.
- Pruebas con herramientas de análisis.
- Pruebas automatizadas.
- Pruebas de usuario.

A manera de ejemplo se realizó una prueba con una herr
amienta de análisis **Accessibility Scanner** o **Test de Accesibilidad**.