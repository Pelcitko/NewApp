package cz.tul.lp.newapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.UUID;

import cz.tul.lp.newapp.R;
import cz.tul.lp.newapp.view.CanvasView;

import static cz.tul.lp.newapp.view.CanvasView.Mode;

public class MainActivity extends AppCompatActivity {

    private CanvasView mCanvasView = null;
    private SeekBar seekBar1 = null, seekBar2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.mCanvasView = (CanvasView)this.findViewById(R.id.canvas);


        this.seekBar1 = (SeekBar)this.findViewById(R.id.seekBar1);
        this.seekBar2 = (SeekBar)this.findViewById(R.id.seekBar2);
        this.seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    mCanvasView.setStrokeWidth(progress);
//                    seek1Changed(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        this.seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    mCanvasView.setOpacity(progress);
//                    seek2Changed(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        setSeekBars(
                Math.round(this.mCanvasView.getStrokeWidth()),
                Math.round(this.mCanvasView.getOpacity()));
    }

    private void seek1Changed(int progress) {
        switch (this.mCanvasView.getMode()){
            case DRAW:
                mCanvasView.setStrokeWidth(progress);
                return;
            case TEXT:
                break;
            case ERASER:
                mCanvasView.setStrokeWidth(progress);
                break;
        }
    }

    private void seek2Changed(int progress) {
        switch (this.mCanvasView.getMode()){
            case DRAW:
                mCanvasView.setOpacity(progress);
                return;
            case TEXT:
                break;
            case ERASER:
                mCanvasView.setOpacity(progress);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Menu selection
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // získání ID vybrané položky
        switch (item.getItemId())
        {
            case R.id.undo:
                mCanvasView.undo();
                return true;

            case R.id.redo:
                mCanvasView.redo();
                return true;

            case R.id.clear:
                mCanvasView.clear();
                return true;

            case R.id.text:
                this.mCanvasView.setMode(Mode.TEXT);
                this.mCanvasView.setText("Canvas View");
                return true;

            case R.id.pencil:
                this.mCanvasView.setMode(Mode.DRAW);
                setSeekBars(
                        Math.round(this.mCanvasView.getPaintStrokeWidth()),
                        Math.round(this.mCanvasView.getPaintOpacity()));
                return true;

            case R.id.eraser:
                this.mCanvasView.setMode(Mode.ERASER);
                setSeekBars(
                        Math.round(this.mCanvasView.getEraserWidth()),
                        Math.round(this.mCanvasView.getEraserOpacity()));
                return true;

            case R.id.archive:
                mCanvasView.setDrawingCacheEnabled(true);
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), mCanvasView.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "drawing");
                if(imgSaved!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                mCanvasView.destroyDrawingCache();
                return true;

            default:
                Toast.makeText(this, item.toString() + " touched", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSeekBars(int level1, int level2) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.seekBar1.setProgress(level1, true);
            this.seekBar2.setProgress(level2, true);
        } else {
            this.seekBar1.setProgress(level1);
            this.seekBar2.setProgress(level2);
        }
    }


    public CanvasView getCanvas() {
        return this.mCanvasView;
    }
}
