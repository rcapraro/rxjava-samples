import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * RxJava samples.
 * Created by rcapraro on 17/12/2015.
 */
public class Samples {

    public static void main(String[] args) {
        System.out.println("Merging async...");
        mergingAsync();
        System.out.println("\nMerging sync...with latency...");
        mergingSync();
        System.out.println("\nMerging sync made async...");
        mergingSyncMadeAsync();
        System.out.println("\nflatMapExampleAsync...");
        flatMapExampleAsync();
        System.out.println("\nflatMapExampleSync...with latency...");
        flatMapExampleSync();
    }

    private static void mergingAsync() {
        Observable.merge(getDataAsync(1), getDataAsync(2), getDataAsync(3)).toBlocking().forEach(System.out::println);
    }

    private static void mergingSync() {
        Observable.merge(getDataSync(1), getDataSync(2), getDataAsync(3)).toBlocking().forEach(System.out::println);
    }

    private static void mergingSyncMadeAsync() {
        Observable
                .merge(getDataSync(1).subscribeOn(Schedulers.computation()),
                        getDataSync(2).subscribeOn(Schedulers.computation()),
                        getDataSync(3).subscribeOn(Schedulers.computation()))
                .toBlocking()
                .forEach(System.out::println);
    }

    private static void flatMapExampleAsync() {
        Observable.range(0, 5).flatMap(i -> getDataAsync(i)).toBlocking().forEach(System.out::println);
    }

    private static void flatMapExampleSync() {
        Observable.range(0, 5).flatMap(i -> getDataSync(i)).toBlocking().forEach(System.out::println);
    }

    static Observable<Integer> getDataAsync(int i) {
        return getDataSync(i).subscribeOn(Schedulers.computation());
    }

    static Observable<Integer> getDataSync(int i) {
        return Observable.create(s -> {
            // simulates latency
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            s.onNext(i);
            s.onCompleted();
        });
    }
}
