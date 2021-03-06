/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.score.stream.drools;

import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.score.constraint.Indictment;
import org.optaplanner.core.api.score.holder.ScoreHolder;
import org.optaplanner.core.impl.score.stream.ConstraintSession;

public class DroolsConstraintSession<Solution_> implements ConstraintSession<Solution_> {

    private final KieSession kieSession;
    private final ScoreHolder scoreHolder;

    public DroolsConstraintSession(KieSession kieSession, ScoreHolder scoreHolder) {
        this.kieSession = kieSession;
        this.scoreHolder = scoreHolder;
    }

    @Override
    public void insert(Object fact) {
        kieSession.insert(fact);
    }

    @Override
    public void update(Object fact) {
        FactHandle factHandle = kieSession.getFactHandle(fact);
        kieSession.update(factHandle, fact);
    }

    @Override
    public void retract(Object fact) {
        FactHandle factHandle = kieSession.getFactHandle(fact);
        kieSession.delete(factHandle);
    }

    // TODO remove in 8.0
    // The scoreHolder method is not actually deprecated for removal.
    // It will only be moved to a different type at a time when we can make that change in public API.
    @SuppressWarnings("deprecation")
    @Override
    public Score<?> calculateScore(int initScore) {
        kieSession.fireAllRules();
        return scoreHolder.extractScore(initScore);
    }

    // TODO remove in 8.0
    // The scoreHolder method is not actually deprecated for removal.
    // It will only be moved to a different type at a time when we can make that change in public API.
    @SuppressWarnings("deprecation")
    @Override
    public Map<String, ConstraintMatchTotal> getConstraintMatchTotalMap() {
        kieSession.fireAllRules();
        return scoreHolder.getConstraintMatchTotalMap();
    }

    // TODO remove in 8.0
    // The scoreHolder method is not actually deprecated for removal.
    // It will only be moved to a different type at a time when we can make that change in public API.
    @SuppressWarnings("deprecation")
    @Override
    public Map<Object, Indictment> getIndictmentMap() {
        kieSession.fireAllRules();
        return scoreHolder.getIndictmentMap();
    }

    @Override
    public void close() {
        kieSession.dispose();
    }

}
