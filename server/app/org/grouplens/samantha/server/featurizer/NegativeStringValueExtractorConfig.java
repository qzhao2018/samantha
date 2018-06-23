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

package org.grouplens.samantha.server.featurizer;

import org.grouplens.samantha.modeler.featurizer.FeatureExtractor;
import org.grouplens.samantha.modeler.featurizer.NegativeStringValueExtractor;
import org.grouplens.samantha.server.io.RequestContext;
import play.Configuration;
import play.inject.Injector;

public class NegativeStringValueExtractorConfig implements FeatureExtractorConfig {
    private final String indexName;
    private final String feaName;
    private final String attrName;
    private final String toReplace;

    private NegativeStringValueExtractorConfig(String indexName,
                                               String attrName,
                                               String feaName,
                                               String toReplace) {
        this.indexName = indexName;
        this.attrName = attrName;
        this.feaName = feaName;
        this.toReplace = toReplace;
    }

    public FeatureExtractor getFeatureExtractor(RequestContext requestContext) {
        return new NegativeStringValueExtractor(indexName, attrName, feaName, toReplace);
    }

    public static FeatureExtractorConfig
    getFeatureExtractorConfig(Configuration extractorConfig,
                              Injector injector) {
        return new NegativeStringValueExtractorConfig(
                extractorConfig.getString("indexName"),
                extractorConfig.getString("attrName"),
                extractorConfig.getString("feaName"),
                extractorConfig.getString("toReplace")
        );
    }
}
