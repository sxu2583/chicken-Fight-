import java.util.Random;
import java.util.Scanner;

public class World {
    public static int hawks_population_default = 20;
    public static int resource_default = 50;
    public static int loss_default = 100;


    public static void argumentsChecker(String[] args){
        if(args.length > 4 || args.length == 0){
            System.err.println("Usage: ./project02 popSize [percentHawks] [resourceAmt] [costHawk-Bird]");
            System.exit(0);
        }
    }

    public static Bird[] generate_population(int size, int percentage){
        //Use default
        double hawk_percentage = percentage / 100.0;
        //if (percentage == -1){ hawk_percentage = hawks_population_default / 100.0; }

        Bird[] birds = new Bird[size];
        double hawks = Math.floor(size * hawk_percentage);
        double doves = size - hawks;


        System.out.println("---------------------------------------");
        System.out.println("Hawk Percent " + hawk_percentage);
        System.out.println("Total Population " + size);
        System.out.println("Hawks "  + hawks);
        System.out.println("Doves " + doves);
        System.out.println("---------------------------------------");


        //Create all birds and set their id numbers
        int hawk_count = (int)hawks;
        int dove_count = (int)doves;
        for(int index = 0; index < size; index++){
            if (hawk_count != 0){
                birds[index] = new Bird("hawk");
                hawk_count -= 1;
            }
            else if (dove_count != 0){
                birds[index] = new Bird("dove");
                dove_count -= 1;
            }
            birds[index].set_id_number(index);
        }
        return birds;
    }


    //Just pass in an array of bird objects and it will check if there are more then 2 alive
    public static boolean is_Alive(Bird[] birds){
        boolean answer = true;
        int count = 0;
        for (int bird = 0 ; bird < birds.length; bird++){
            if (birds[bird].alive == 1){count += 1; }
        }
        if (count < 2){ answer = false; }
        return answer;
    }

    public static int how_many_alive(Bird[] birds){
        int count = 0;
        for (int bird = 0; bird < birds.length; bird++){
            if (birds[bird].alive == 1){
                count += 1;
            }
        }
        return count;
    }

    public static Bird[] random_Pick(Bird[] birds, int size){
        Bird[] chosen = new Bird[2];
        Random random = new Random();

        int min = 0;
        int num1 = random.nextInt(size - min) + min;
        int num2 = random.nextInt(size - min) + min;

        //Make sure we always have different numbers
        while (num1 == num2){num2 = random.nextInt(size - min) + min;}

        //Check to see if there are more then 2 birds alive
        if (how_many_alive(birds) > 2) {
            //Make sure our 2 chosen birds are alive
            while (birds[num1].alive == 0 || birds[num2].alive == 0) {
                num1 = random.nextInt(size - min) + min;
                num2 = random.nextInt(size - min) + min;
            }
            chosen[0] = birds[num1];
            chosen[1] = birds[num2];
        } else {
            //All dead or one alive
            chosen = null;
        }
        return chosen;
    }

    public static void menu(){
        System.out.println("===============MENU=============");
        System.out.println("1 ) Starting Stats");
        System.out.println("2 ) Display Individuals and Points");
        System.out.println("3 ) Display Sorted");
        System.out.println("4 ) Have 1000 interactions");
        System.out.println("5 ) Have 10000 interactions");
        System.out.println("6 ) Have N interactions");
        System.out.println("7 ) Step through interactions 'Stop' to return to menu");
        System.out.println("8 ) Quit");
        System.out.println("================================");
    }

    public static void status(int population, int hawks_percentage, int hawk_count, int resource, int loss){

        System.out.println("" +
                "Population size: " + population + "\n" +
                "Percentage of Hawks: " + hawks_percentage + "%\n" +
                "Number of Hawks: " + hawk_count +"\n" +
                "\n" +
                "Percentage of Doves: " + (100 - hawks_percentage) + "%\n" +
                "Number of Doves: " + (population - hawk_count) + "\n" +
                "\n" +
                "Each resource is worth: " + resource + "\n" +
                "Cost of Hawk-Hawk interaction: " + loss);
    }

    public static Bird[] update_encounter(Bird[] birds){
        for (int bird = 0; bird < birds.length; bird++){
            birds[bird].update_encounter();
        }
        return birds;
    }


    public static Bird[] interaction(Bird[] birds, int resource_amount, int loss, int size){
        Bird[] all_birds = birds;
        Scanner scanner = new Scanner(System.in);

        //todo: make sure interactions works before the while
        System.out.println("Interaction done");
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("Stop")){
                break;
            }

            Bird[] bird_pair = random_Pick(birds, size);
            if (bird_pair == null) {
                System.out.println("Not enough Alive birds to continue");
                break;
            }

            //Print out the encounter number
            System.out.println("Encounter " + bird_pair[0].encounter);

            //If both birds are doves
            if (bird_pair[0].strategy.equals("dove") && bird_pair[1].strategy.equals("dove")){
                System.out.println("Two Doves");
            }

            //If one hawk and one dove
            if (bird_pair[0].strategy.equals("hawk") && bird_pair[1].strategy.equals("dove")){
                System.out.println("One hawk and one dove");
            }

            //If one dove and one hawk
            if (bird_pair[0].strategy.equals("dove") && bird_pair[1].strategy.equals("hawk")){
                System.out.println("One dove and one hawk");
            }

            //If both birds are hawks
            if (bird_pair[0].strategy.equals("hawk") && bird_pair[1].strategy.equals("hawk")){
                System.out.println("Two hawks");
            }

            //Increment encounter of all birds by 1
            all_birds = update_encounter(all_birds);
        }
        return all_birds;
    }
}
