package pt.andrexdias.sensores;

import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {

    private List<Sensor> sensorList;

    public SensorAdapter(List<Sensor> sensorList) {
        this.sensorList = sensorList;
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sensor_item, parent, false);
        return new SensorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        Sensor sensor = sensorList.get(position);
        holder.nameTextView.setText(sensor.getName());
        holder.vendorTextView.setText("Fornecedor: " + sensor.getVendor());
        holder.typeTextView.setText("Tipo: " + sensor.getType());
        holder.powerTextView.setText("Consumo: " + sensor.getPower() + " mA");
    }

    @Override
    public int getItemCount() {
        return sensorList.size();
    }

    public static class SensorViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView vendorTextView;
        public TextView typeTextView;
        public TextView powerTextView;

        public SensorViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.sensor_name);
            vendorTextView = itemView.findViewById(R.id.sensor_vendor);
            typeTextView = itemView.findViewById(R.id.sensor_type);
            powerTextView = itemView.findViewById(R.id.sensor_power);
        }
    }
}