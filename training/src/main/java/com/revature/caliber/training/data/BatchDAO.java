package com.revature.caliber.training.data;

        import com.revature.caliber.training.beans.Batch;

        import java.util.List;

/**
 * Batch Dao interface for data tier.
 */
public interface BatchDAO {

    public void createBatch(Batch batch);
    public List<Batch> getAll();
    public List<Batch> getTrainerBatch(String name);
    public List<Batch> getCurrentBatch();
    public List<Batch> getCurrentBatch(String name);
    public Batch getBatch(Integer id);
    public void updateBatch(Batch batch);
    public void deleteBatch(Batch batch);
}