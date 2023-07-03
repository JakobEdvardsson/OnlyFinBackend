package se.onlyfin.onlyfin2backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.onlyfin.onlyfin2backend.model.Subscription;
import se.onlyfin.onlyfin2backend.model.SubscriptionId;
import se.onlyfin.onlyfin2backend.model.User;
import se.onlyfin.onlyfin2backend.repository.SubscriptionRepository;
import se.onlyfin.onlyfin2backend.service.UserService;

import java.security.Principal;

/**
 * This class is responsible for handling requests related to subscriptions.
 */
@RequestMapping("/subscriptions")
@CrossOrigin(origins = "localhost:3000", allowCredentials = "true")
@Controller
public class SubscriptionController {
    private final UserService userService;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionController(UserService userService, SubscriptionRepository subscriptionRepository) {
        this.userService = userService;
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Adds a subscription for the logged-in user to a user with the given username.
     *
     * @param principal      The logged-in user.
     * @param targetUsername The username of the user to subscribe to.
     * @return 200 OK if the subscription was successful, 404 Not Found if the target user does not exist.
     */
    @PutMapping("/add")
    public ResponseEntity<?> subscribe(Principal principal, @RequestParam String targetUsername) {
        User actingUser = userService.getUserOrException(principal.getName());

        User targetUser = userService.getUserOrNull(targetUsername);
        if (targetUser == null) {
            return ResponseEntity.notFound().build();
        }

        SubscriptionId subscriptionId = new SubscriptionId();
        subscriptionId.setSubscriber(actingUser);
        subscriptionId.setSubscribedTo(targetUser);
        Subscription subscription = new Subscription();
        subscription.setId(subscriptionId);

        subscriptionRepository.save(subscription);

        return ResponseEntity.ok().build();
    }

    /**
     * Removes a subscription for the logged-in user from a user with the given username.
     *
     * @param principal      The logged-in user.
     * @param targetUsername The username of the user to unsubscribe from.
     * @return 200 OK if the unsubscription was successful, 404 Not Found if the target user does not exist.
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> unsubscribe(Principal principal, @RequestParam String targetUsername) {
        User actingUser = userService.getUserOrException(principal.getName());

        User targetUser = userService.getUserOrNull(targetUsername);
        if (targetUser == null) {
            return ResponseEntity.notFound().build();
        }

        SubscriptionId subscriptionId = new SubscriptionId();
        subscriptionId.setSubscriber(actingUser);
        subscriptionId.setSubscribedTo(targetUser);

        subscriptionRepository.deleteById(subscriptionId);

        return ResponseEntity.ok().build();
    }

}
