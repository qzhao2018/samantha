/*
 * Copyright (c) [2016-2018] [University of Minnesota]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.grouplens.samantha.server.retriever;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.grouplens.samantha.modeler.dao.EntityDAO;
import org.grouplens.samantha.server.common.JsonHelpers;
import org.grouplens.samantha.server.dao.EntityDAOUtilities;
import org.grouplens.samantha.server.expander.ExpanderUtilities;
import org.grouplens.samantha.server.io.RequestContext;
import play.Configuration;
import play.inject.Injector;

import java.util.ArrayList;
import java.util.List;

public class RequestBasedRetriever extends AbstractRetriever {
    private final Configuration entityDaoConfigs;
    private final Injector injector;
    private final String daoConfigKey;

    public RequestBasedRetriever(Configuration entityDaoConfigs, RequestContext requestContext,
                                 Injector injector, String daoConfigKey, Configuration config) {
        super(config, requestContext, injector);
        this.entityDaoConfigs = entityDaoConfigs;
        this.injector = injector;
        this.daoConfigKey = daoConfigKey;
    }

    public RetrievedResult retrieve(RequestContext requestContext) {
        JsonNode reqBody = requestContext.getRequestBody();
        List<ObjectNode> hits = new ArrayList<>();
        if (reqBody.has(daoConfigKey)) {
            JsonNode reqDao = JsonHelpers.getRequiredJson(requestContext.getRequestBody(), daoConfigKey);
            EntityDAO entityDAO = EntityDAOUtilities.getEntityDAO(entityDaoConfigs, requestContext,
                    reqDao, injector);
            while (entityDAO.hasNextEntity()) {
                hits.add(entityDAO.getNextEntity());
            }
            hits = ExpanderUtilities.expand(hits, expanders, requestContext);
        }
        return new RetrievedResult(hits, hits.size());
    }
}
