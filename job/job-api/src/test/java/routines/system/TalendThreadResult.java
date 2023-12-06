/*
 * #%L
 * Talend :: ESB :: Job :: API
 * %%
 * Copyright (c) 2006-2021 Talend Inc. - www.talend.com
 * %%
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
 * #L%
 */
// ============================================================================
//
// Copyright (c) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 5/7 rue Salomon De Rothschild, 92150 Suresnes, France
//
// ============================================================================
package routines.system;

public class TalendThreadResult {

    private Integer errorCode = null;

    private String status = ""; //$NON-NLS-1$

    private Exception exception = null;

    public Exception getException() {
        return this.exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    // only keep the max error code
    public void setErrorCode(Integer errorCode) {
        if (errorCode != null) {
            if (this.errorCode == null || errorCode.compareTo(this.errorCode) > 0) {
                this.errorCode = errorCode;
            }
        }
    }

    public String getStatus() {
        return status;
    }

    // status will be "" , "failure" or "end"
    public void setStatus(String status) {
        this.status = status;
    }

}
