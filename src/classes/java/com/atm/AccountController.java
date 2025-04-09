package com.atm;

import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccountController
{
    @FXML public AnchorPane anchor;
    @FXML public HBox anchorHbox;
    @FXML public Button homeButton;
    @FXML public Button walletButton;
    @FXML public Button profileButton;

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
    public ScrollPane rulerScrollPane;
    public HBox rulerContainer;

    @FXML private ScrollPane horizontalScroll;
    @FXML private HBox buttonContainer;

    // transaction scene

    @FXML public Button exitButton;
    @FXML public AnchorPane transactionScene;

    @FXML public ScrollPane cardHorizontalScroll;
    @FXML public HBox cardContainer;
    @FXML public AnchorPane cardItemContainer;
    @FXML public AnchorPane cardItemBlue;
    @FXML public AnchorPane cardItemGrey;

    @FXML public TextField recipientTextField;

    @FXML public ScrollPane payeesHorizontalScroll;
    @FXML public HBox payeesContainer;
    @FXML public AnchorPane payeeItemContainer;
    @FXML public AnchorPane payeeItemBlue;
    @FXML public AnchorPane payeeItemGrey;

    private AnchorPane selectedCard = null;

    private AnchorPane selectedPayeeNode = null;
    private Account selectedPayeeAccount = null;

    private final List<AnchorPane> allCards = new ArrayList<>();
    private final List<AnchorPane> allPayees = new ArrayList<>();

    private Controller controller;
    private Model model;

    private States accountState;
    private States inputState;
    private States transactionState;

    private double translateX = 0;

    private final List<Node> spheres = new ArrayList<>();



    public void initialize(Controller controller, Model model)
    {
        this.transactionState = States.DEFAULT;
        this.accountState = States.DEFAULT;
        this.inputState = States.DEFAULT;

        this.controller = controller;
        this.model = model;

        homeButton.setId("Home-page");
        walletButton.setId("Wallet-page");
        profileButton.setId("Profile-page");

        sendButton.setId("Transaction-page");
        receiveButton.setId("Transaction-page2");
        rewardButton.setId("Transaction-page2");
        exitButton.setId("Close-page");

        anchor.setTranslateX(464);
        transactionScene.setTranslateY(896);

        BindButtons();
        BindScrolls();
        BindTextFields();
    }

    public void BindScrolls()
    {
        ScrollPane[] scrollPanes = {
                horizontalScroll, cardHorizontalScroll, payeesHorizontalScroll
        };

        HBox[] hContainers = {
                buttonContainer, cardContainer, payeesContainer
        };

        // was quite challenging for researching an alternative way for using horizontal scrollable pane...
        // that barely works compared to vertical scroll.
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
                homeButton, walletButton, profileButton, exitButton
        };

        AnchorPane[] anchors = {
                sendButton, receiveButton, rewardButton
        };

        for (Button button : buttons) {
            registerButton(button);
        }

        for (AnchorPane anchor : anchors) {
            registerAnchor(anchor);
        }
    }

    public void BindTextFields()
    {
        TextField[] textFields = {
                recipientTextField
        };

        for (TextField textField : textFields) {
            registerTextField(textField);
        }
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
        node.setOpacity(0);

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
            if (node != selectedCard)
            {
                hoverIn.playFromStart();
            }
        });

        node.setOnMouseExited(event ->
        {
            if (node != selectedCard)
            {
                hoverOut.playFromStart();
            }
        });

        node.setOnMouseClicked(event ->
        {
            if (node != selectedCard)
            {
                // Fade out the others
                for (AnchorPane otherCard : allCards)
                {
                    if (otherCard != node)
                    {
                        Timeline fadeOutOthers = new Timeline(
                                new KeyFrame(Duration.seconds(0.2),
                                        new KeyValue(otherCard.opacityProperty(), 0, new SineInterpolator()))
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

                this.selectedCard = (AnchorPane) node;
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
            if (node != selectedPayeeNode) {
                hoverIn.playFromStart();
            }
        });

        node.setOnMouseExited(event ->
        {
            if (node != selectedPayeeNode) {
                hoverOut.playFromStart();
            }
        });

        node.setOnMouseClicked(event ->
        {
            if (node != selectedPayeeNode)
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

                selectedPayeeNode = (AnchorPane) node;
                selectedPayeeAccount = payee;

                controller.process("Transaction-page-select", payee);
            }
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
           // controller.process("Clear-Input");
            controller.process("Open-Input");
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
            case TRANSACTION_PAGE -> 72;
            default -> 896;
        };

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

    private void updateCardHolder(Account account)
    {
        // Method for updating the cardholder's card from the homepage

        // Fetching the current account's name, and extracting into first or last name.
        String username = account.getName();
        String firstName = username.split(" ")[0];

        this.username.setText(username);
        this.firstName.setText(firstName);

        double balance = account.getBalance();

        // Using javafx number formatter that converts numbers into formated and parsing strings.
        // Format the balance with initial £, comma separators for each group, and two decimal places as pences.
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        String formattedBalance = formatter.format(balance);

        this.accountBalance.setText(formattedBalance);

        int PIN = account.getNumber();

        this.accountPIN.setText("No. " + PIN);

        Types type = account.getType();

        this.accountType.setText(type + " Account");

        Card currentCard = account.getCard();
        String companyName = currentCard.companyName();
        Types cardType = currentCard.cardType();

        this.accountCompany.setText(cardType + " " + companyName);
        this.accountCompanyIcon.setImage(View.getCardImage(companyName));
    }

    private void updateTransaction(States transactionState, Account account)
    {
        States inputState = this.model.getState(Scene.INPUT);
        int input = this.model.getInput();

        if (inputState.equals(States.INPUT_DIGIT))
        {
            String stringInput = String.valueOf(input);

            if (!stringInput.equals("0"))
            {
                recipientTextField.setText(String.valueOf(input));
            }
            else
            {
                recipientTextField.setText("Enter 6 digit PIN code");
            }

            if (!recipientTextField.getStyleClass().contains("pinpoint")) {
                recipientTextField.getStyleClass().add("pinpoint");
            }
        }
        else
        {
            if (recipientTextField.getStyleClass().contains("pinpoint")) {
                recipientTextField.getStyleClass().remove("pinpoint");
            }
        }

        payeesContainer.getChildren().clear();
        allPayees.clear();

        Account myAccount = this.model.getAccount();
        ArrayList<Account> accounts = this.model.findAccountPartially(input);
        boolean isSelectedSeen = false;

        for (Account payee : accounts)
        {
            // Skip for the iteration, since the owner shouldn't find their account as payee.
            if (myAccount.equals(payee))
            {
              continue;
            };

            AnchorPane payeeItemContainer = new AnchorPane();
            payeeItemContainer.getStyleClass().addAll(payeeItemContainer.getStyleClass());
            payeeItemContainer.setPrefSize(payeeItemContainer.getPrefWidth(), payeeItemContainer.getPrefHeight());

            AnchorPane bluePayee = buildPayeePane(payee, payeeItemBlue, false);
            AnchorPane greyPayee = buildPayeePane(payee, payeeItemGrey, true);

            boolean isSelected = selectedPayeeAccount != null && selectedPayeeAccount.equals(payee);

            if (isSelected)
            {
                isSelectedSeen = true;
                bluePayee.setOpacity(1);
                selectedPayeeNode = bluePayee; // rebind the node to this fresh one
                selectedPayeeAccount = payee;
            }
            else
            {
                bluePayee.setOpacity(0);
            }

            payeeItemContainer.getChildren().addAll(greyPayee, bluePayee);

            registerPayee(bluePayee, payee);

            payeesContainer.getChildren().add(payeeItemContainer);
        }

        if (!isSelectedSeen)
        {
            selectedPayeeNode = null;
            selectedPayeeAccount = null;
        }

        if (this.transactionState.equals(transactionState))
        {
            return;
        }
        else
        {
            this.transactionState = transactionState;
        }

        cardContainer.getChildren().clear();
        allCards.clear();

        // Javafx doesn't support cloning, which I had to do manual duplication instead.
        // by using two different helper methods.
        for (Card card : account.getCards())
        {
            AnchorPane cardItemContainer = new AnchorPane();
            cardItemContainer.getStyleClass().addAll(cardItemContainer.getStyleClass());
            cardItemContainer.setPrefSize(cardItemContainer.getPrefWidth(), cardItemContainer.getPrefHeight());

            AnchorPane blueCard = buildCardPane(card, cardItemBlue, false);
            AnchorPane greyCard = buildCardPane(card, cardItemGrey, true);

            cardItemContainer.getChildren().addAll(greyCard, blueCard);

            registerCard(blueCard, card);

            cardContainer.getChildren().add(cardItemContainer);
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
                updateTransaction(transactionState, account);
            }
        }
    }

    // Node cloning helper
    private Node cloneNode(Node node)
    {
        if (node instanceof Text original)
        {
            Text copy = new Text(original.getText());
            copy.setId(original.getId());
            copy.setLayoutX(original.getLayoutX());
            copy.setLayoutY(original.getLayoutY());
            copy.setOpacity(original.getOpacity());
            copy.getStyleClass().addAll(original.getStyleClass());
            return copy;
        }
        else if (node instanceof ImageView original)
        {
            ImageView copy = new ImageView();
            copy.setId(original.getId());
            copy.setFitHeight(original.getFitHeight());
            copy.setFitWidth(original.getFitWidth());
            copy.setLayoutX(original.getLayoutX());
            copy.setLayoutY(original.getLayoutY());
            copy.setPreserveRatio(original.isPreserveRatio());
            copy.getStyleClass().addAll(original.getStyleClass());
            return copy;
        }
        else if (node instanceof Circle original) {
            Circle copy = new Circle();
            copy.setId(original.getId());
            copy.setRadius(original.getRadius());
            copy.setLayoutX(original.getLayoutX());
            copy.setLayoutY(original.getLayoutY());
            copy.getStyleClass().addAll(original.getStyleClass());
            return copy;
        }
        else if (node instanceof Button original) {
            Button copy = new Button();
            copy.setId(original.getId());
            copy.setPrefWidth(original.getPrefWidth());
            copy.setLayoutX(original.getLayoutX());
            copy.setLayoutY(original.getLayoutY());
            copy.getStyleClass().addAll(original.getStyleClass());
            return copy;
        }
        else if (node instanceof VBox original) {
            VBox copy = new VBox();
            copy.setId(original.getId());
            copy.setPrefWidth(original.getPrefWidth());
            copy.setPrefHeight(original.getPrefHeight());
            copy.setLayoutX(original.getLayoutX());
            copy.setLayoutY(original.getLayoutY());
            copy.setAlignment(original.getAlignment());
            copy.getStyleClass().addAll(original.getStyleClass());
            return copy;
        }

        // Fallback for unknown types
        return new Group();
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
                        text.setText(card.cardType() + " " + card.companyName());
                    }
                    if (text.getId().equals("cardNumber"))
                    {
                        text.setText("No. " + card.cardNumber());
                    }
                }
                case ImageView imageView when imageView.getId().equals("cardIcon") ->
                {
                    String icon = isGrey ? card.companyName() + "-grey" : card.companyName();
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
