/**
 * <p>
 * This module contains the main logic for the requirements,
 * so the functionality can integrate with other persistence and
 * output modules, different from current <code>infrastructure</code> one.
 * </p>
 * <p>
 * There are two main interfaces for accessing the functionality:
 * <ul>
 * <li><code>GetAds</code>: for accessing the ads according to different criteria (public/quality).</li>
 * <li><code>UpdateRanking</code>: for invoking the score calculation.</li>
 * </ul>
 * </p>
 * Persistence layer will have to provide an adapter implementation for this interface:
 * <ul>
 * <li>AdsSource: used for accessing the persistence required functionality.</li>
 * </ul>
 * <p>
 * Aside from these interfaces, the module provide other two packages:
 * <ul>
 * <li><code>domain</code>: contains the definition for the domain entities.</li>
 * <li><code>ranking</code>: contains the logic for calculating the score of an add.</li>
 * </ul>
 * </p>
 * @since 1.0.0
 * @author José Ángel García
 * @version 1.0.0
 */
package com.idealista.application;

