package com.atm;

import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.atm.States.DEFAULT;
import static com.atm.States.TRANSACTION_COMPLETE;

public class AccountController
{
    @FXML public AnchorPane anchor;

    @FXML public HBox anchorHbox;
    @FXML public Button homeButton;
    @FXML public Button walletButton;
    @FXML public Button profileButton;

    @FXML public ScrollPane verticalScroll;
    @FXML public VBox homeContainer;

    @FXML public Text username;
    @FXML public Text firstName;

    @FXML public Text accountType;
    @FXML public Text accountPIN;
    @FXML public Text accountBalance;
    @FXML public Text accountCompany;
    @FXML public ImageView accountCompanyIcon;

    @FXML public AnchorPane sendButton;
    @FXML public AnchorPane receiveButton;
    @FXML public AnchorPane rewardButton;

    @FXML public VBox transactionContainer;
    @FXML public AnchorPane transactionItem;
    @FXML public AnchorPane transactionDate;

    // summary transaction scene

    @FXML public Button exitButton1;
    @FXML public AnchorPane transactionSummaryScene;
    @FXML public PieChart pieChartTransaction;
    @FXML public Text sortButton;

    @FXML public VBox transactionContainer1;
    @FXML public AnchorPane transactionItem1;
    @FXML public AnchorPane transactionDate1;

    private String[] sorts = {"All", "Income", "Expenses"};
    private int sortIndex = 0;

    // review scene

    @FXML public AnchorPane transactionReviewScene;
    @FXML public Button exitButton2;
    @FXML public Button sendCashButton1;

    @FXML public Text payerType;
    @FXML public Text payerUsername;
    @FXML public Text payerCompany;
    @FXML public Text payerPIN;
    @FXML public ImageView payerCompanyIcon;
    @FXML public Text payerBalance;

    @FXML public Text payeeType;
    @FXML public Text payeeUsername;
    @FXML public Text payeeCompany;
    @FXML public Text payeePIN;
    @FXML public ImageView payeeCompanyIcon;

    @FXML public Text reviewAmount;
    @FXML public Text reviewDate;

    // transaction completion scene;

    @FXML public AnchorPane transactionCompleteScene;

    // transaction scene

    @FXML public AnchorPane transactionScene;
    @FXML public Button exitButton;
    @FXML public Text seeMoreFButton;
    @FXML public Text seeMoreWButton;
    @FXML public Text seeMoreDButton;

    @FXML public ScrollPane cardHorizontalScroll;
    @FXML public HBox cardContainer;
    @FXML public AnchorPane cardItemContainer;
    @FXML public AnchorPane cardItemBlue;
    @FXML public AnchorPane cardItemGrey;
    @FXML public Text textSelectedCard;

    @FXML public TextField recipientTextField;

    @FXML public ScrollPane payeesHorizontalScroll;
    @FXML public HBox payeesContainer;
    @FXML public AnchorPane payeeItemContainer;
    @FXML public AnchorPane payeeItemBlue;
    @FXML public AnchorPane payeeItemGrey;
    @FXML public Text textSelectedPayee;
    @FXML public Text textSelectedBalance;

    @FXML public TextField amountTextField;
    @FXML public Button sendCashButton;

    private AnchorPane selectedCardNode = null;
    private Card selectedCard = null;
    private Card prevSelectedCard = null;

    private AnchorPane selectedPayeeNode = null;
    private Account selectedPayeeAccount = null;
    private Account prevSelectedPayeeAccount = null;

    private final List<AnchorPane> allCards = new ArrayList<>();
    private final List<AnchorPane> allPayees = new ArrayList<>();

    private Controller controller;
    private Model model;

    private States accountState;

    private double translateX = 0;
    private double translateY = 0;

    public void initialize(Controller controller, Model model)
    {
        this.accountState = States.DEFAULT;

        this.controller = controller;
        this.model = model;

        homeButton.setId("Home-page");
        walletButton.setId("Wallet-page");
        profileButton.setId("Profile-page");

        sendButton.setId("Transaction-page");
        //receiveButton.setId("Transaction-page2");
       // rewardButton.setId("Transaction-page2");

        seeMoreFButton.setId("Transaction-list");
        seeMoreWButton.setId("Transaction-summary");
        seeMoreDButton.setId("Transaction-list");

        exitButton.setId("Transaction-page-close");
        recipientTextField.setId("Open-Input");
        amountTextField.setId("Open-Input-Balance");
        sendCashButton.setId("Transaction-page");

        exitButton1.setId("Transaction-summary-close");

        exitButton2.setId("Transaction-page-close");
        sendCashButton1.setId("Transaction-page");

        anchor.setTranslateX(464);
        transactionScene.setTranslateY(896);
        transactionReviewScene.setTranslateY(896);

        BindButtons();
        BindScrolls();
        BindTextFields();
    }

    public void BindScrolls()
    {
        ScrollPane[] scrollPanes = {
                cardHorizontalScroll, payeesHorizontalScroll
        };

        HBox[] hContainers = {
                cardContainer, payeesContainer
        };

        ScrollPane[] vScrollPanes = {
                verticalScroll
        };

        VBox[] vContainers = {
                homeContainer
        };

        // was quite challenging for researching an alternative way for using horizontal scrollable pane...
        // that barely works compared to vertical scroll.
        for (int index = 0; index < vScrollPanes.length; index++)
        {
            ScrollPane currentScrollPane = vScrollPanes[index];
            VBox currentVContainers = vContainers[index];

            int multiply = Integer.parseInt(currentScrollPane.getId());

            currentScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
                if (event.getDeltaY() != 0)
                {
                    // Fetch the scroll delta whenever the end-user scrolls up or down.
                    // Up is positive, while down is negative output.
                    double delta = event.getDeltaY();
                    translateY -= delta * multiply;

                    // Clamping to avoid over-scrolling
                    double minTranslate = Math.min(0, currentScrollPane.getHeight() - currentVContainers.getHeight());
                    translateY = Math.max(minTranslate, Math.min(0, translateY));

                    // Animate movement
                    TranslateTransition transition = new TranslateTransition(Duration.millis(150), currentVContainers);
                    transition.setInterpolator(new SineInterpolator());
                    transition.setToY(translateY);
                    transition.play();

                    event.consume(); // Prevent default scroll
                }
            });
        }

        for (int index = 0; index < scrollPanes.length; index++)
        {
            ScrollPane currentScrollPane = scrollPanes[index];
            HBox currentHContainers = hContainers[index];

            int multiply = Integer.parseInt(currentScrollPane.getId());

            currentScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
                if (event.getDeltaY() != 0)
                {
                    // Fetch the scroll delta whenever the end-user scrolls up or down.
                    // Up is positive, while down is negative output.
                    double delta = event.getDeltaY();
                    translateX -= delta * multiply;

                    // Clamping to avoid over-scrolling
                    double minTranslate = Math.min(0, currentScrollPane.getWidth() - currentHContainers.getWidth());
                    translateX = Math.max(minTranslate, Math.min(0, translateX));

                    // Animate movement
                    TranslateTransition transition = new TranslateTransition(Duration.millis(150), currentHContainers);
                    transition.setInterpolator(new SineInterpolator());
                    transition.setToX(translateX);
                    transition.play();

                    event.consume(); // Prevent default scroll
                }
            });
        }
    }

    public void BindButtons()
    {

        Button[] buttons = {
                homeButton, walletButton, profileButton, exitButton, sendCashButton, sendCashButton1,
                exitButton1, exitButton2
        };

        AnchorPane[] anchors = {
                sendButton
        };

        Text[] texts = {
                seeMoreFButton, seeMoreWButton, seeMoreDButton
        };

        for (Button button : buttons)
        {
            registerButton(button);
        }

        for (AnchorPane anchor : anchors)
        {
            registerAnchor(anchor);
        }

        for (Text text : texts)
        {
            registerText(text);
        }

        registerSort();
    }

    public void BindTextFields()
    {
        TextField[] textFields = {
                recipientTextField, amountTextField
        };

        for (TextField textField : textFields) {
            registerTextField(textField);
        }
    }

    private void hoverText(Text text)
    {
        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(text.opacityProperty(), .6, new SineInterpolator())
                )
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(text.opacityProperty(), 1, new SineInterpolator())
                )
        );

        text.setOnMouseEntered(event -> {
            hoverAnimation.playFromStart();
        });
        text.setOnMouseExited(event -> {
            exitAnimation.playFromStart();
        });
    }

    private void hoverButton(Button button)
    {
        // Initially ensuring that the buttons are unselected (with the greyed-out effect)
        button.setOpacity(0);

        // Timelines for hover and exit (transition from gray to blue)
        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(button.opacityProperty(), 1, new SineInterpolator())
                )
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(button.opacityProperty(), 0, new SineInterpolator())
                )
        );

        // I had tried to find different solutions, although implementing them are quite complicated,
        // in which I decided to practically create two buttons, where the top one fades in or out.

        button.setOnMouseEntered(event -> {
            hoverAnimation.playFromStart();
        });
        button.setOnMouseExited(event -> {
            exitAnimation.playFromStart();
        });
    }

    private void hoverAnchor(AnchorPane anchor)
    {
        anchor.setOpacity(0);

        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(anchor.opacityProperty(), 1, new SineInterpolator())
                )
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(anchor.opacityProperty(), 0, new SineInterpolator())
                )
        );

        anchor.setOnMouseEntered(event -> {
            hoverAnimation.playFromStart();
        });
        anchor.setOnMouseExited(event -> {
            exitAnimation.playFromStart();
        });
    }

    // Similar to hoverButton, but exclusively for any other elements
    private void hoverFadeCards(Node node, Card card)
    {
        Timeline hoverIn = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(node.opacityProperty(), 1, new SineInterpolator())
                )
        );

        Timeline hoverOut = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(node.opacityProperty(), 0, new SineInterpolator())
                )
        );

        node.setOnMouseEntered(event ->
        {
            if (node != selectedCardNode)
            {
                hoverIn.playFromStart();
            }
        });

        node.setOnMouseExited(event ->
        {
            if (node != selectedCardNode)
            {
                hoverOut.playFromStart();
            }
        });

        node.setOnMouseClicked(event ->
        {
            if (node != selectedCardNode)
            {
                // Fade out the others
                for (AnchorPane cardNode : allCards)
                {
                    if (cardNode != node)
                    {
                        Timeline fadeOutOthers = new Timeline(
                                new KeyFrame(Duration.seconds(0.2),
                                        new KeyValue(cardNode.opacityProperty(), 0, new SineInterpolator()))
                        );
                        fadeOutOthers.playFromStart();
                    }
                }

                // Fade in the selected one
                Timeline fadeInSelected = new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(node.opacityProperty(), 1, new SineInterpolator()))
                );

                fadeInSelected.playFromStart();

                this.selectedCardNode = (AnchorPane) node;

                if (this.selectedCard != null)
                {
                    this.prevSelectedCard = this.selectedCard;
                }

                this.selectedCard = card;

                this.controller.process("Transaction-page-select", card);
            }
        });
    }

    private void hoverFadePayee(Node node, Account payee)
    {
        Timeline hoverIn = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(node.opacityProperty(), 1, new SineInterpolator())
                )
        );

        Timeline hoverOut = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(node.opacityProperty(), 0, new SineInterpolator())
                )
        );

        node.setOnMouseEntered(event ->
        {
            if (node != this.selectedPayeeNode) {
                hoverIn.playFromStart();
            }
        });

        node.setOnMouseExited(event ->
        {
            if (node != this.selectedPayeeNode) {
                hoverOut.playFromStart();
            }
        });

        node.setOnMouseClicked(event ->
        {
            if (node != this.selectedPayeeNode)
            {
                // Fade out the others
                for (AnchorPane payeeNode : allPayees)
                {
                    if (payeeNode != node)
                    {
                        Timeline fadeOutOthers = new Timeline(
                                new KeyFrame(Duration.seconds(0.2),
                                        new KeyValue(payeeNode.opacityProperty(), 0, new SineInterpolator()))
                        );
                        fadeOutOthers.playFromStart();
                    }
                }

                // Fade in the selected one
                Timeline fadeInSelected = new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(node.opacityProperty(), 1, new SineInterpolator()))
                );

                fadeInSelected.playFromStart();

                if (this.selectedPayeeAccount != null)
                {
                    prevSelectedPayeeAccount = this.selectedPayeeAccount;
                }

                this.selectedPayeeNode = (AnchorPane) node;
                this.selectedPayeeAccount = payee;

                this.controller.process("Transaction-page-select", payee);
            }
        });
    }

    public void registerText(Text text)
    {
        hoverText(text);

        text.setOnMouseClicked(event ->
        {
            controller.process(text.getId());
        });
    }

    public void registerSort()
    {
        hoverText(sortButton);

        sortButton.setOnMouseClicked(event ->
        {
            sortButton.setText(sorts[sortIndex]);

            sortIndex = (sortIndex + 1) % sorts.length;

            this.update();
        });
    }

    private void registerCard(AnchorPane anchorCard, Card card)
    {
        allCards.add(anchorCard);
        hoverFadeCards(anchorCard, card);
    }

    private void registerPayee(AnchorPane anchorPayee, Account payee)
    {
        allPayees.add(anchorPayee);
        hoverFadePayee(anchorPayee, payee);
    }

    private void registerTextField(TextField textField)
    {
        textField.setOnMouseClicked(event ->
        {
            controller.process(textField.getId());
        });
    }
   // MGA2D007256162442
   //         MGA2D007256176269
   // MGA2D007256184545
    private void registerButton(Button button)
    {
        //TODO: Add ripple effect
        button.setOnMousePressed(event ->
        {
            this.controller.process(button.getId());
        });
        hoverButton(button);
    }

    private void registerAnchor(AnchorPane anchor)
    {
        anchor.setOnMousePressed(event ->
        {
            this.controller.process(anchor.getId());
        });
        hoverAnchor(anchor);
    }

    private void slideTransaction(States transactionState)
    {
        double targetY = switch (transactionState)
        {
            case TRANSACTION_PAGE, TRANSACTION_CONFIRM, TRANSACTION_COMPLETE -> 72;
            default -> 896;
        };

        double targetYreview = switch (transactionState)
        {
            case TRANSACTION_CONFIRM, TRANSACTION_COMPLETE -> 72;
            default -> 896;
        };

        double targetNComplete = switch (transactionState)
        {
            case TRANSACTION_COMPLETE -> 1;
            default -> 0;
        };

        double targetYsummary = switch (transactionState)
        {
            case TRANSACTION_SUMMARY -> 72;
            default -> 896;
        };

        Timeline transactionCompleteAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(transactionCompleteScene.opacityProperty(), targetNComplete, new SineInterpolator())
                )
        );

        transactionCompleteScene.setMouseTransparent(transactionState != TRANSACTION_COMPLETE);

        transactionCompleteAnimation.playFromStart();

        Timeline transactionReviewAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(transactionReviewScene.translateYProperty(), targetYreview, new SineInterpolator())
                )
        );

        transactionReviewAnimation.playFromStart();

        Timeline transactionSummaryAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(transactionSummaryScene.translateYProperty(), targetYsummary, new SineInterpolator())
                )
        );

        transactionSummaryAnimation.playFromStart();

        Timeline transactionAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(transactionScene.translateYProperty(), targetY, new SineInterpolator())
                )
        );

        transactionAnimation.playFromStart();
    }

    public void slide(States state)
    {
        if (this.accountState.equals(state))
        {
            return;
        }

        this.accountState = state;

        double targetAnchor = switch (state)
        {
            case HOME_PAGE, WALLET_PAGE, PROFILE_PAGE -> 0;
            default -> 464;
        };

        double targetHbox = switch (state)
        {
            case WALLET_PAGE -> -424;
            case PROFILE_PAGE -> -424 * 2;
            default -> 0;
        };

        anchor.setMouseTransparent(state == States.DEFAULT);

        Timeline anchorAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateXProperty(), targetAnchor, new SineInterpolator())
                )
        );

        Timeline hboxAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchorHbox.translateXProperty(), targetHbox, new SineInterpolator())
                )
        );

        hboxAnimation.play();
        anchorAnimation.play();
    }

    private void updateTransactionSummary(Account account)
    {
        List<History> historyList = account.getHistory();

        // using java's stream to manipulate the history list sequentially with filter and mapping.
        double sumGain = historyList.stream().
                filter(value -> value.amount() > 0).
                mapToDouble(History::amount).
                sum();

        double sumLoss = historyList.stream().
                filter(value -> value.amount() < 0).
                mapToDouble(History::amount).
                sum();

        pieChartTransaction.getData().clear();

        PieChart.Data gainData = new PieChart.Data("Gain", sumGain);
        PieChart.Data lossData = new PieChart.Data("Loss", sumLoss);

        gainData.getNode().setStyle("-fx-pie-color: #199EFF;");
        lossData.getNode().setStyle("-fx-pie-color: #E66100;");

        pieChartTransaction.getData().addAll(gainData, lossData);

        Card currentCard = account.getCard();

        transactionContainer1.getChildren().clear();

        String lastDate = null;

        //almost forgotten that java starts 0 and not 1 lol

        double total = historyList.stream().
                filter(history -> history.card().equals(currentCard)).
                mapToDouble(History::amount).
                sum();

        List<History> filteredhistoryList = switch (this.sortIndex)
        {
            // Including all transactions.
            case 0 -> historyList.stream().
                    filter(history -> history.card().equals(currentCard)).
                    toList();
            // Income transactions only.
            case 1 -> historyList.stream().
                    filter(history -> history.card().equals(currentCard)).
                    filter(value -> value.amount() > 0).
                    toList();
            // Expense transactions only.
            case 2 -> historyList.stream().
                    filter(history -> history.card().equals(currentCard)).
                    filter(value -> value.amount() < 0).
                    toList();
            default -> throw new IllegalStateException("Unexpected value: " + this.sortIndex);
        };

        for (History history : filteredhistoryList)
        {
            String currentDate = history.date();
            // If the date is different from the last date, add a date header.
            if (!currentDate.equals(lastDate))
            {
                AnchorPane dateHeader = buildTransactionDate(currentDate, total, transactionDate1);
                transactionContainer1.getChildren().add(dateHeader);
                lastDate = currentDate;
            }

            AnchorPane transaction = buildTransactionPane(
                    history.payee(),
                    history.date(),
                    history.time(),
                    history.amount(),
                    transactionItem1
            );

            transactionContainer1.getChildren().add(transaction);
        }
    }

    private void updateTransactionConfirmation(Account account)
    {
        Card currentCard = account.getCard();

        String username = account.getName();

        this.payerUsername.setText(username);

        double balance = currentCard.getCardBalance();

        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        String formattedBalance = formatter.format(balance);

        this.payerBalance.setText(formattedBalance);

        String sort = currentCard.getCardSort();
        String number = currentCard.getCardNumber();

        this.payerPIN.setText(sort + " " + number);

        Types type = account.getType();

        this.payerType.setText(type + " Account");

        String companyName = currentCard.getCardCompany();
        Types cardType = currentCard.getCardType();

        this.payerCompany.setText(cardType + " " + companyName);
        this.payeeCompanyIcon.setImage(View.getCardImage(companyName));

        System.out.println(this.selectedPayeeAccount);

        // Update the payee's preview card
        if (this.selectedPayeeAccount != null)
        {
            Account payeeAccount = this.selectedPayeeAccount;
            Card payeeCard = payeeAccount.getCard();

            this.payeeUsername.setText(payeeAccount.getName());

            int payeeNumberLength = payeeCard.getCardNumber().length();
            String payeeNumber = payeeCard.getCardNumber().substring(payeeNumberLength - 3);

            this.payeePIN.setText("**-**-** ***" + payeeNumber);

            this.payeeType.setText(payeeAccount.getType() + " Account");

            String payeeCompanyName = payeeCard.getCardCompany();
            Types payeeCardType = payeeCard.getCardType();

            this.payeeCompany.setText(payeeCardType + " " + payeeCompanyName);
            this.payeeCompanyIcon.setImage(View.getCardImage(payeeCompanyName));
        }
    }

    private void updateCardHolder(Account account)
    {
        // Method for updating the cardholder's card from the homepage

        Card currentCard = account.getCard();

        // Fetching the current account's name, and extracting into first or last name.
        String username = account.getName();
        String firstName = username.split(" ")[0];

        this.username.setText(username);
        this.firstName.setText(firstName);

        double balance = currentCard.getCardBalance();

        // Using javafx number formatter that converts numbers into formated and parsing strings.
        // Format the balance with initial £, comma separators for each group, and two decimal places as pences.
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        String formattedBalance = formatter.format(balance);

        this.accountBalance.setText(formattedBalance);

        String sort = currentCard.getCardSort();
        String number = currentCard.getCardNumber();

        this.accountPIN.setText(sort + " " + number);

        Types type = account.getType();

        this.accountType.setText(type + " Account");

        String companyName = currentCard.getCardCompany();
        Types cardType = currentCard.getCardType();

        this.accountCompany.setText(cardType + " " + companyName);
        this.accountCompanyIcon.setImage(View.getCardImage(companyName));

        List<History> historyList = account.getHistory();

        transactionContainer.getChildren().clear();

        String lastDate = null;
        double total = 0;

        for (History history : historyList)
        {
            if (!history.card().equals(currentCard))
            {
                break;
            }
            total =+ history.amount();
        }

        for (History history : historyList)
        {
            if (!history.card().equals(currentCard))
            {
                break;
            }

            String currentDate = history.date();
            // If the date is different from the last date, add a date header.
            if (!currentDate.equals(lastDate))
            {
                AnchorPane dateHeader = buildTransactionDate(currentDate, total, transactionDate);
                transactionContainer.getChildren().add(dateHeader);
                lastDate = currentDate;
            }

            AnchorPane transaction = buildTransactionPane(
                    history.payee(),
                    history.date(),
                    history.time(),
                    history.amount(),
                    transactionItem
            );

            transactionContainer.getChildren().add(transaction);
        }
    }

    private void updateTransaction(Account account)
    {
        States inputState = this.model.getState(Scene.INPUT);

        long input = this.model.getInput();
        double balance = this.model.getInputBalance();

        if (inputState.equals(States.INPUT_DIGIT))
        {
            String stringInput = String.valueOf(input);

            if (!stringInput.equals("0"))
            {
                this.recipientTextField.setText(stringInput);
            }
            else
            {
                this.recipientTextField.setText("Enter 6 digit PIN code");
            }

            // Since each input automatically updates this method.
            // we must track the current styles before removing or applying them

            ObservableList<String> styleClasses = this.recipientTextField.getStyleClass();

            // Assume that the styleClass of the recipient contains more than 3 styles.
            if (!styleClasses.isEmpty())
            {
                // Assume that the last style applied should be the one to be replaced
                // with another style.

                int lastIndex = styleClasses.size() - 1;
                String lastStyle = styleClasses.get(lastIndex);

                // Assume the last style is it's base style
                if (lastStyle.equals("baseTextField"))
                {
                    styleClasses.set(lastIndex, "baseTextField-pinpoint");
                }
            }
        }
        else
        {
            // Same to the previous technique, except we revert the style of the textfield.
            ObservableList<String> styleClasses = this.recipientTextField.getStyleClass();

            if (!styleClasses.isEmpty())
            {
                int lastIndex = styleClasses.size() - 1;
                String lastStyle = styleClasses.get(lastIndex);

                if (lastStyle.equals("baseTextField-pinpoint"))
                {
                    styleClasses.set(lastIndex, "baseTextField");
                }
            }
        }

        if (inputState.equals(States.INPUT_BALANCE))
        {
            String stringBalance = String.format("%.2f", balance);

            if (!stringBalance.equals("0.00"))
            {
                this.amountTextField.setText("£" + stringBalance);
            }
            else
            {
                this.amountTextField.setText("Enter the amount");
            }

            ObservableList<String> styleClasses = this.amountTextField.getStyleClass();

            if (!styleClasses.isEmpty())
            {
                int lastIndex = styleClasses.size() - 1;
                String lastStyle = styleClasses.get(lastIndex);

                if (lastStyle.equals("baseTextField"))
                {
                    styleClasses.set(lastIndex, "baseTextField-pinpoint");
                }
            }
        }
        else
        {
            ObservableList<String> styleClasses = this.amountTextField.getStyleClass();

            if (!styleClasses.isEmpty())
            {
                int lastIndex = styleClasses.size() - 1;
                String lastStyle = styleClasses.get(lastIndex);

                if (lastStyle.equals("baseTextField-pinpoint"))
                {
                    styleClasses.set(lastIndex, "baseTextField");
                }
            }
        }

        if (this.selectedCard != null)
        {
            this.textSelectedCard.setText(this.selectedCard.getCardSort() + " " +  this.selectedCard.getCardNumber());
            this.textSelectedBalance.setText("£" +  this.selectedCard.getCardBalance());
        }
        else
        {
            this.textSelectedCard.setText("None");
            this.textSelectedBalance.setText("£0.00");
        }

        if (this.selectedPayeeAccount != null)
        {
            this.textSelectedPayee.setText( this.selectedPayeeAccount.getName());
        }
        else
        {
            this.textSelectedPayee.setText("No one");
        }

        this.payeesContainer.getChildren().clear();
        this.allPayees.clear();

        ArrayList<Account> accounts = this.model.findAccountPartially(input);
        boolean isSelectedSeen = false;

        for (Account payee : accounts)
        {
            // Skip for the iteration, since the owner shouldn't find their account as payee.
            if (account.equals(payee))
            {
              continue;
            };

            AnchorPane payeeItemContainer = new AnchorPane();
            payeeItemContainer.getStyleClass().addAll(payeeItemContainer.getStyleClass());
            payeeItemContainer.setPrefSize(payeeItemContainer.getPrefWidth(), payeeItemContainer.getPrefHeight());

            AnchorPane bluePayee = buildPayeePane(payee, payeeItemBlue, false);
            AnchorPane greyPayee = buildPayeePane(payee, payeeItemGrey, true);

            bluePayee.setOpacity(0);

            boolean isSelected =  this.selectedPayeeAccount != null && this.selectedPayeeAccount.equals(payee);
            boolean isPrevSelected = this.prevSelectedPayeeAccount != null && this.prevSelectedPayeeAccount.equals(payee);

            if (isSelected)
            {
                isSelectedSeen = true;
                bluePayee.setOpacity(1);
                this.selectedPayeeNode = bluePayee; // rebind the node to this fresh one
                this.selectedPayeeAccount = payee;
            }

            if (isPrevSelected)
            {
                this.prevSelectedPayeeAccount = null;
                bluePayee.setOpacity(1);
                Timeline fadeOutOthers = new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(bluePayee.opacityProperty(), 0, new SineInterpolator()))
                );
                fadeOutOthers.playFromStart();
            }

            payeeItemContainer.getChildren().addAll(greyPayee, bluePayee);

            registerPayee(bluePayee, payee);

            this.payeesContainer.getChildren().add(payeeItemContainer);
        }

        if (!isSelectedSeen || accounts.isEmpty())
        {
            this.selectedPayeeNode = null;
            this.selectedPayeeAccount = null;
            this.prevSelectedPayeeAccount = null;
        }

        this.cardContainer.getChildren().clear();
        this.allCards.clear();

        List<Card> cards = account.getCards();

        // Javafx doesn't support cloning, which I had to do manual duplication instead.
        // by using two different helper methods.
        for (Card card : cards)
        {
            AnchorPane cardItemContainer = new AnchorPane();
            cardItemContainer.getStyleClass().addAll(cardItemContainer.getStyleClass());
            cardItemContainer.setPrefSize(cardItemContainer.getPrefWidth(), cardItemContainer.getPrefHeight());

            AnchorPane blueCard = buildCardPane(card, cardItemBlue, false);
            AnchorPane greyCard = buildCardPane(card, cardItemGrey, true);

            blueCard.setOpacity(0);

            boolean isSelected = this.selectedCard != null && Objects.equals(this.selectedCard.getCardNumber(), card.getCardNumber());
            boolean isPrevSelected = this.prevSelectedCard != null && Objects.equals(this.prevSelectedCard.getCardNumber(), card.getCardNumber());

            if (isSelected)
            {
                blueCard.setOpacity(1);
                this.selectedCardNode = blueCard; // rebind the node to this fresh one
                this.selectedCard = card;
            }

            if (isPrevSelected)
            {
                this.prevSelectedCard = null;
                blueCard.setOpacity(1);
                Timeline fadeOutOthers = new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(blueCard.opacityProperty(), 0, new SineInterpolator()))
                );
                fadeOutOthers.playFromStart();
            }

            cardItemContainer.getChildren().addAll(greyCard, blueCard);

            registerCard(blueCard, card);

            this.cardContainer.getChildren().add(cardItemContainer);
        }

        if (cards.isEmpty())
        {
            this.selectedCardNode = null;
            this.selectedCard = null;
            this.prevSelectedCard = null;
        }
    }

    public void update()
    {
        Account account = this.model.getAccount();

        if (account != null)
        {
            States accountState = this.model.getState(Scene.ACCOUNT);
            States transactionState = this.model.getState(Scene.TRANSACTION);

            slideTransaction(transactionState);

            // Only update these elements necessarily based on the scene shown.
            if (accountState.equals(States.HOME_PAGE))
            {
                updateCardHolder(account);
            }

            if (transactionState.equals(States.TRANSACTION_PAGE))
            {
                updateTransaction(account);
            }
            else if (transactionState.equals(DEFAULT))
            {
                    this.recipientTextField.setText("Enter 6 digit PIN code");
                    this.amountTextField.setText("Enter the amount");

                    this.selectedCardNode = null;
                    this.selectedCard = null;
                    this.prevSelectedCard = null;
                    this.allCards.clear();

                    this.selectedPayeeNode = null;
                    this.selectedPayeeAccount = null;
                    this.prevSelectedPayeeAccount = null;
                    this.allPayees.clear();
            }

            System.out.println(transactionState);

            if (transactionState.equals(States.TRANSACTION_CONFIRM))
            {
                updateTransactionConfirmation(account);
            }

            if (transactionState.equals(States.TRANSACTION_SUMMARY))
            {
                updateTransactionSummary(account);
            }
        }
    }

    // Node cloning helper
    private Node cloneNode(Node node)
    {
        Node copy = null;

        if (node instanceof Text original) {
            Text text = new Text(original.getText());
            text.setId(original.getId());
            text.setLayoutX(original.getLayoutX());
            text.setLayoutY(original.getLayoutY());
            text.setOpacity(original.getOpacity());
            text.getStyleClass().addAll(original.getStyleClass());
            copy = text;

        } else if (node instanceof AnchorPane original) {
            AnchorPane pane = new AnchorPane();
            pane.setId(original.getId());
            pane.setPrefHeight(original.getPrefHeight());
            pane.setPrefWidth(original.getPrefWidth());
            pane.setLayoutX(original.getLayoutX());
            pane.setLayoutY(original.getLayoutY());
            pane.getStyleClass().addAll(original.getStyleClass());
            pane.setPadding(original.getPadding());
            copy = pane;

        } else if (node instanceof ImageView original) {
            ImageView image = new ImageView();
            image.setId(original.getId());
            image.setFitHeight(original.getFitHeight());
            image.setFitWidth(original.getFitWidth());
            image.setLayoutX(original.getLayoutX());
            image.setLayoutY(original.getLayoutY());
            image.setPreserveRatio(original.isPreserveRatio());
            image.getStyleClass().addAll(original.getStyleClass());
            copy = image;

        } else if (node instanceof Circle original) {
            Circle circle = new Circle();
            circle.setId(original.getId());
            circle.setRadius(original.getRadius());
            circle.setLayoutX(original.getLayoutX());
            circle.setLayoutY(original.getLayoutY());
            circle.getStyleClass().addAll(original.getStyleClass());
            copy = circle;

        } else if (node instanceof Button original) {
            Button button = new Button();
            button.setId(original.getId());
            button.setPrefWidth(original.getPrefWidth());
            button.setLayoutX(original.getLayoutX());
            button.setLayoutY(original.getLayoutY());
            button.getStyleClass().addAll(original.getStyleClass());
            copy = button;

        } else if (node instanceof VBox original) {
            VBox vbox = new VBox();
            vbox.setId(original.getId());
            vbox.setPrefWidth(original.getPrefWidth());
            vbox.setPrefHeight(original.getPrefHeight());
            vbox.setLayoutX(original.getLayoutX());
            vbox.setLayoutY(original.getLayoutY());
            vbox.setAlignment(original.getAlignment());
            vbox.setPadding(original.getPadding());
            vbox.getStyleClass().addAll(original.getStyleClass());
            copy = vbox;

        } else if (node instanceof BorderPane original) {
            BorderPane border = new BorderPane();
            border.setId(original.getId());
            border.setPrefWidth(original.getPrefWidth());
            border.setPrefHeight(original.getPrefHeight());
            border.setLayoutX(original.getLayoutX());
            border.setLayoutY(original.getLayoutY());
            border.getStyleClass().addAll(original.getStyleClass());
            border.setPadding(original.getPadding());

            if (original.getTop() != null) border.setTop(cloneNode(original.getTop()));
            if (original.getBottom() != null) border.setBottom(cloneNode(original.getBottom()));
            if (original.getLeft() != null) border.setLeft(cloneNode(original.getLeft()));
            if (original.getRight() != null) border.setRight(cloneNode(original.getRight()));
            if (original.getCenter() != null) border.setCenter(cloneNode(original.getCenter()));

            copy = border;
        }

        // Try copying margin if the parent exists and the parent uses it.
        if (copy != null && node.getParent() != null) {
            Insets margin = null;

            if (node.getParent() instanceof VBox) {
                margin = VBox.getMargin(node);
                if (margin != null) VBox.setMargin(copy, margin);
            } else if (node.getParent() instanceof HBox) {
                margin = HBox.getMargin(node);
                if (margin != null) HBox.setMargin(copy, margin);
            } else if (node.getParent() instanceof BorderPane) {
                // BorderPane doesn't use margin the same way.
            }
        }

        return (copy != null) ? copy : new Group();
    }

    private AnchorPane buildTransactionPane(Account payee, String date, String time, double amount, AnchorPane template)
    {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().addAll(template.getStyleClass());
        pane.setPrefSize(template.getPrefWidth(), template.getPrefHeight());

        for (Node instance : template.getChildren())
        {
            Node copy = cloneNode(instance);

            switch (copy)
            {
                case Text text -> {
                    if (text.getId().equals("transactionName"))
                    {
                        text.setText(payee.getName());
                    }
                    if (text.getId().equals("transactionDate"))
                    {
                        text.setText(date);
                    }
                    if (text.getId().equals("transactionTime"))
                    {
                        text.setText(time);
                    }
                    if (text.getId().equals("transactionAmount"))
                    {
                        DecimalFormat formatter = new DecimalFormat("#,###.##"); // might add into time formatter

                        String formatted = formatter.format(amount);

                        text.setText("+ £" + formatted);
                    }
                }
                case ImageView imageView when imageView.getId().equals("transactionIcon") ->
                {
                    String icon = "profile";
                    imageView.setImage(View.getCardImage(icon));
                }
                default -> {}
            }

            pane.getChildren().add(copy);
        }

        return pane;
    }

    private AnchorPane buildTransactionDate(String date, double total, AnchorPane template)
    {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().addAll(template.getStyleClass());
        pane.setPrefSize(template.getPrefWidth(), template.getPrefHeight());

        for (Node instance : template.getChildren())
        {
            Node copy = cloneNode(instance);

            if (copy instanceof Text text) {
                if (text.getId().equals("transactionDate"))
                {
                    text.setText(date);
                }
                if (text.getId().equals("transactionTotal"))
                {
                    DecimalFormat formatter = new DecimalFormat("#,###.##"); // might add into time formatter

                    String formatted = formatter.format(total);

                    text.setText("£" + formatted);
                }
            }

            pane.getChildren().add(copy);
        }

        return pane;
    }

    private AnchorPane buildCardPane(Card card, AnchorPane template, boolean isGrey)
    {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().addAll(template.getStyleClass());
        pane.setPrefSize(template.getPrefWidth(), template.getPrefHeight());

        for (Node instance : template.getChildren())
        {
            Node copy = cloneNode(instance);

            switch (copy)
            {
                case Text text -> {
                    if (text.getId().equals("cardName"))
                    {
                        text.setText(card.getCardType() + " " + card.getCardCompany());
                    }
                    if (text.getId().equals("cardNumber"))
                    {
                        text.setText(card.getCardSort() + " " + card.getCardNumber());
                    }
                }
                case ImageView imageView when imageView.getId().equals("cardIcon") ->
                {
                    String icon = isGrey ? card.getCardCompany() + "-grey" : card.getCardCompany();
                    imageView.setImage(View.getCardImage(icon));
                }
                default -> {}
            }

            pane.getChildren().add(copy);
        }

        return pane;
    }

    private AnchorPane buildPayeePane(Account payee, AnchorPane template, boolean isGrey)
    {
        AnchorPane pane = new AnchorPane();
        pane.getStyleClass().addAll(template.getStyleClass());
        pane.setPrefSize(template.getPrefWidth(), template.getPrefHeight());

        for (Node instance : template.getChildren())
        {
            // Since some instances are parented with the current instance,
            // I had to manually iterate through them, clone them and then parent them back.
            Node copy = recursiveCloneAndProcess(instance, payee, isGrey);
            pane.getChildren().add(copy);
        }

        return pane;
    }

    private Node recursiveCloneAndProcess(Node original, Account payee, boolean isGrey)
    {
        Node copy = cloneNode(original); // Assuming this clone layout and basic props

        switch (copy)
        {
            case Text text -> {
                if ("payeeName".equals(text.getId()))
                {
                    String firstName = payee.getName().split(" ")[0];
                    text.setText(firstName);
                }
            }
            case ImageView imageView when "payeeIcon".equals(imageView.getId()) -> {
                String icon = isGrey ? "profile-grey" : "profile";
                imageView.setImage(View.getCardImage(icon));
            }
            case Parent parent -> {
                // Recursively clone and process all children of the container node
                ObservableList<Node> newChildren = ((Pane) copy).getChildren();
                for (Node child : ((Pane) original).getChildren())
                {
                    newChildren.add(recursiveCloneAndProcess(child, payee, isGrey));
                }
            }
            default -> {}
        }

        return copy;
    }
}
