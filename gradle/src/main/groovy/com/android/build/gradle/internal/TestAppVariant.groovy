/*
 * Copyright (C) 2012 The Android Open Source Project
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
package com.android.build.gradle.internal

import com.android.build.gradle.AndroidBasePlugin
import com.android.builder.AndroidBuilder
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.compile.Compile
import com.android.builder.VariantConfiguration

class TestAppVariant implements ApplicationVariant {
    final String name
    final VariantConfiguration config
    FileCollection runtimeClasspath
    FileCollection resourcePackage
    Compile compileTask

    TestAppVariant(VariantConfiguration config) {
        this.config = config
        if (config.hasFlavors()) {
            this.name = "${config.firstFlavor.name.capitalize()}Test"
        } else {
            this.name = "Test"
        }
    }

    @Override
    String getDescription() {
        if (config.hasFlavors()) {
            return "Test build for the ${config.firstFlavor.name.capitalize()}${config.buildType.name.capitalize()} build"
        } else {
            return "Test for the ${config.buildType.name.capitalize()} build"
        }
    }

    String getDirName() {
        if (config.hasFlavors()) {
            return "$config.firstFlavor.name/test"
        } else {
            return "test"
        }
    }

    String getBaseName() {
        if (config.hasFlavors()) {
            return "$config.firstFlavor.name-test"
        } else {
            return "test"
        }
    }

    @Override
    boolean getZipAlign() {
        return false
    }

    @Override
    boolean isSigned() {
        return true;
    }

    @Override
    boolean getRunProguard() {
        return false
    }

    @Override
    List<String> getRunCommand() {
        String[] args = [ "shell", "am", "instrument", "-w",
                config.getPackageName() + "/" + config.instrumentationRunner]

        return Arrays.asList(args)
    }

    @Override
    String getPackage() {
        return config.getPackageName()
    }

    @Override
    AndroidBuilder createBuilder(AndroidBasePlugin androidBasePlugin) {
        AndroidBuilder androidBuilder = new AndroidBuilder(
                androidBasePlugin.sdkParser,
                androidBasePlugin.logger,
                androidBasePlugin.verbose)

        androidBuilder.setTarget(androidBasePlugin.target)
        androidBuilder.setVariantConfig(config)

        return androidBuilder
    }
}
