import java.util.List;

public interface Repository<S, U, T> { //Farkli data tipinde objelerle calisabilsin diye jenerik yapalim


    void cereateTable();

    void save(S entity);

    List<S> findall();

    void update(S entity, U id);

    void deletedById(U id);

    S findById(U id);

    S findByNameLastName(T name);


}
