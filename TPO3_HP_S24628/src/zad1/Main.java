/**
 * @author Harasiuk Paweł S24628
 */

package zad1;


import static zad1.Time.passed;

public class Main {

    public static void main(String[] args) throws Exception {
        String fileName = System.getProperty("user.home") + "/PassTimeOptions.yaml";
        Options opts = Tools.createOptionsFromYaml(fileName);

        System.out.println(passed("2000-01-01", "2020-04-01"));
        System.out.println(passed("2018-01-01", "2020-02-02"));
        System.out.println(passed("2019-01-01", "2020-04-03"));
        System.out.println(passed("2020-04-01T10:00", "2020-04-01T13:00"));
        System.out.println(passed("2020-03-27T10:00", "2020-03-28T10:00")); // przed zmianą czasu
        System.out.println(passed("2020-03-28T10:00", "2020-03-29T10:00")); // po zmianie czasu
        System.out.println(passed("2020-03-28T10", "2020-03-29T10:00"));
        System.out.println(passed("2019-02-29", "2020-04-03"));

        System.out.println(opts);
        opts.getClientsMap().forEach((id, dates) -> {
            System.out.println(id);
            dates.forEach(dpair -> {
                String[] d = dpair.split(" +");
                String info = passed(d[0], d[1]);
                System.out.println(info);
            });
        });
    }

}
