package net.iessanclemente.pmdm.u3_student_manu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.iessanclemente.pmdm.utiles.Constantes;
import net.iessanclemente.pmdm.utiles.Utiles;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText editTextPrincipal;
    private CheckBox checkBoxClear;
    private TextView textLabel;
    private Button botonAnadirTexto;
    private RadioButton radioButtonRed;
    private RadioButton radioButtonBlue;

    private Button botonCronometro;
    private Chronometer cronometro;
    private ImageView imagenMonumento;
    private Spinner listaProvincias;

    private Boolean esPrimerInicio;
    private Boolean esStartCronometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        esPrimerInicio = Boolean.TRUE;
        esStartCronometro = Boolean.TRUE;

        // Primera seccion
        editTextPrincipal = findViewById(R.id.editTextTextPersonName);
        checkBoxClear = findViewById(R.id.checkBoxClear);
        textLabel = findViewById(R.id.textLabel);
        botonAnadirTexto = findViewById(R.id.buttonAddClear);
        radioButtonRed = findViewById(R.id.radioButtonRed);
        radioButtonBlue = findViewById(R.id.radioButtonBlue);
        botonAnadirTexto.setOnClickListener(this::onClickTexto);

        botonCronometro = findViewById(R.id.buttonCronometro);
        botonCronometro.setOnClickListener(this::onClickCronometro);

        cronometro = findViewById(R.id.cronometro);

        imagenMonumento = findViewById(R.id.imageViewMonumento);
        if (imagenMonumento != null) {
            imagenMonumento.setOnClickListener(this::onclickImagenMonumento);
        }

        listaProvincias = findViewById(R.id.spinnerProvincias);

        // Añadir listener para lista
        listaProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String TAG = "onItemSelected:";
                if (esPrimerInicio) {
                    Log.i(TAG, "No mostramos el Toast porque acabamos de entrar en la APP");
                    esPrimerInicio = Boolean.FALSE;
                } else {
                    String provincia = listaProvincias.getSelectedItem().toString();
                    // Comprobamos si la provincia es de galicia
                    if (Arrays.asList(getResources().getStringArray(R.array.provincias_gallegas_array)).contains(provincia)) {
                        Log.i(TAG, "La provincia seleccionada es Gallega");
                        Toast.makeText(MainActivity.this, R.string.text_toast_gal, Toast.LENGTH_LONG).show();
                    } else {
                        Log.i(TAG, "La provincia seleccionada NO es Gallega");
                        Toast.makeText(MainActivity.this, R.string.text_toast_no_gal, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final String TAG = "onNothingSelected:";
                Log.i(TAG, "No se seleciona ninguna provincia");
            }
        });

        // Cronometro
        cronometro.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                final String TAG = "onChronometerTick:";

                long tiempoPasado = SystemClock.elapsedRealtime() - chronometer.getBase();
                int tiempoSeg = (int) tiempoPasado / Constantes.CALCULO_ML_TO_S;
                if (Constantes.TIEMPO_AUTODESTRUCCION_APP.equals(tiempoSeg)) {
                    Log.i(TAG, "Se cierra la aplicación porque pasaron los ".concat(Constantes.TIEMPO_AUTODESTRUCCION_APP.toString()).concat(" segundos del cronometro."));
                    finish();
                }

            }
        });

    }

    /**
     * Evento OnClick para el boton de añadir borrar texto
     * @param view
     */
    private void onClickTexto (View view) {

        // Comprobar selección color
        if (this.radioButtonRed.isChecked()) {
            this.textLabel.setTextColor(Color.RED);
        } else if (this.radioButtonBlue.isChecked()) {
            this.textLabel.setTextColor(Color.BLUE);
        }

        if (this.checkBoxClear.isChecked()) {
            // Limpiar texto
            this.textLabel.setText(StringUtils.EMPTY);
        } else {
            this.textLabel.append(
                    StringUtils.isNotBlank(this.editTextPrincipal.getText())
                    ? StringUtils.isBlank(this.textLabel.getText())
                            ? Utiles.toUpperCaseFrase(this.editTextPrincipal.getText().toString())
                            : StringUtils.SPACE.concat(this.editTextPrincipal.getText().toString())
                    : StringUtils.EMPTY);
        }
    }

    /**
     * Evento on click para imagen monumento
     * @param view
     */
    private void onclickImagenMonumento(View view) {
        final String TAG = "onclickImagenMonumento:";
        Log.i(TAG, "Se hace click en la img");
        Toast.makeText(MainActivity.this,R.string.text_image,Toast.LENGTH_LONG).show();

    }

    /**
     * Evento OnClick para el Cronometro
     * @param view
     */
    private void onClickCronometro(View view) {

        // Comprobar estado cronometro
        if (this.esStartCronometro) {
            this.cronometro.setBase(SystemClock.elapsedRealtime());
            this.cronometro.start();
            this.botonCronometro.setText(R.string.text_stop);
            this.esStartCronometro = Boolean.FALSE;
        } else {
            this.cronometro.stop();
            this.cronometro.setBase(SystemClock.elapsedRealtime());
            this.botonCronometro.setText(R.string.text_start);
            this.esStartCronometro = Boolean.TRUE;
        }

    }
}